package com.dama.controller.api;

import com.dama.model.dto.BeaconDto;
import com.dama.model.dto.response.ItemStompTotalResponseDto;
import com.dama.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StompController {

    private final SimpMessagingTemplate template;

    private final ItemService itemService;
    private static String itemCode;
    public static HashMap<String,Integer> hashMap=new HashMap<>();
    public static int totalPrice;
    public static int totalCount;
    public static boolean ItemState;
    public static boolean itemCountIfZero=false;


    @PostMapping("/api/websocket/state")
    public void state(@RequestBody BeaconDto beaconDto){
//        System.out.println("ob_name = " + beaconDto.getItemCode());
        ItemState=true;
        itemCode= beaconDto.getItemCode();
    }

    @PostMapping("/api/websocket/weight")
    public void weight(@RequestBody BeaconDto beaconDto){
        ItemState=false;
        itemCode=beaconDto.getItemCode();
//        if (!hashMap.isEmpty()){
//            hashMap.remove(itemCode);
//        }
//        if (hashMap.get(itemCode)==null){
//            itemCountIfZero=true;
//
    }

    /*@PostMapping("/api/websocket/gps")
    public void gps(@RequestParam("gps") double weight){
        System.out.println("weight = " + weight);
    }*/

    @MessageMapping("/api/websocket/itemWeight/{username}")
    public void weightStomp(@DestinationVariable String username) throws InterruptedException{
        ItemStompTotalResponseDto i=new ItemStompTotalResponseDto();
//        System.out.println("ItemState = " + ItemState);
//        System.out.println("itemCountIfZero = " + itemCountIfZero);
//        System.out.println("itemCode = " + itemCode);
//        if (ItemState==true){

//        }else if (ItemState==false){
        if (ItemState){
            if (itemCode!=null){
                totalCount+=1;
                totalPrice+=itemService.returnItemState(itemCode).getPrice();
            }
        }
        if (!ItemState) {
            if (itemCode != null&&!hashMap.isEmpty()) {
                totalPrice -= itemService.itemPricetoTotalPrice(itemCode);
                totalCount -= 1;
            }
        }
//            if (itemCountIfZero) i.setItemCode(itemCode);
//        }
        i.setTotalPrice(totalPrice);
        i.setTotalCount(totalCount);
//        i.setIfZero(itemCountIfZero);
        template.convertAndSend("/sub/item/weight/"+username,i);
//        itemCountIfZero=false;
        itemCode=null;
    }



    @MessageMapping("/api/websocket/itemList/{username}")
    public void enter(@DestinationVariable String username) throws InterruptedException {
        System.out.println("ItemState = " + ItemState);
        if (ItemState&&itemCode!=null){
            hashMap.put(itemCode, hashMap.getOrDefault(itemCode, 0));
        }

//        System.out.println("itemCode = " + itemCode);
//        System.out.println("hashMap = " + hashMap);
        if (hashMap.containsKey(itemCode)&&ItemState&&itemCode!=null) {
//            System.out.println("count :" + hashMap.get(itemCode));
            hashMap.put(itemCode,hashMap.get(itemCode)+1);
        }
        if (hashMap.containsKey(itemCode)&&!ItemState&&itemCode!=null){
            hashMap.put(itemCode,hashMap.get(itemCode)-1);
        }
        System.out.println("itemCode = " + itemCode);
        if (ItemState&&itemCode!=null){
            ResponseEntity<?> returnRespEntity = itemService.findItemStateByItemCodeToWebSocket(itemCode,hashMap.get(itemCode));
            System.out.println("returnDto = " +returnRespEntity.getStatusCode());
            template.convertAndSend("/sub/chat/read/"+username,returnRespEntity);
        }
//        System.out.println("username = " + username);
        itemCode=null;
        ItemState=true;
    }

    public HashMap<String, Integer> returnHashmap(){

        return hashMap;
    }

    public void returnTotalCount(int tC){
        System.out.println("tC = " + tC);
        totalCount=tC;
    }

    public void returnTotalPrice(int tP){
        System.out.println("tP = " + tP);
        totalPrice=tP;
    }
}
