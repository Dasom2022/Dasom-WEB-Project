package com.dama.controller.api;

import com.dama.model.dto.BeaconDto;
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
    private static int temp=0;
    private static String itemCode;
    public static HashMap<String,Integer> hashMap=new HashMap<>();

    public static int totalPrice;
    public static int totalCount;


    @PostMapping("/api/websocket/state")
    public void state(@RequestBody BeaconDto beaconDto){
        System.out.println("ob_name = " + beaconDto.getItemCode());
        itemCode= beaconDto.getItemCode();
    }

    @PostMapping("/api/websocket/weight")
public void weight(@RequestParam("weight") double weight){
        System.out.println("weight = " + weight);
    }

    /*@PostMapping("/api/websocket/gps")
    public void gps(@RequestParam("gps") double weight){
        System.out.println("weight = " + weight);
    }*/

    @MessageMapping("/api/websocket/itemList/{username}")
    public void enter(@DestinationVariable String username) throws InterruptedException {
        hashMap.put(itemCode, hashMap.getOrDefault(itemCode, 0));
        System.out.println("hashMap = " + hashMap);
        if (hashMap.containsKey(itemCode)) {
            System.out.println("count :" + hashMap.get(itemCode));
            hashMap.put(itemCode,hashMap.get(itemCode)+1);
        }
        System.out.println("itemCode = " + itemCode);
        if (itemCode!=null){
            System.out.println("hashMap = " + hashMap.get(itemCode));
            System.out.println("=======================================================");
            System.out.println("temp = " + temp);
            totalCount+=hashMap.get(itemCode)-temp;
            temp=hashMap.get(itemCode);
            totalPrice+=itemService.returnItemState(itemCode).getPrice();
        }else {
            totalCount+=0;
            totalPrice+=0;
        }
        ResponseEntity<?> returnRespEntity = itemService.findItemStateByItemCodeToWebSocket(itemCode,hashMap.get(itemCode),totalCount,totalPrice);
        System.out.println("username = " + username);
        System.out.println("returnDto = " +returnRespEntity.getStatusCode());
        template.convertAndSend("/sub/chat/read/"+username,returnRespEntity);
        itemCode=null;
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
