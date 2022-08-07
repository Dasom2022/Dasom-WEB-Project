package com.dama.controller.api;

import com.dama.model.dto.BeaconDto;
import com.dama.model.dto.response.ItemStompTotalResponseDto;
import com.dama.model.dto.response.QRLoginResponseDto;
import com.dama.model.dto.response.QRLoginStompResponseDTO;
import com.dama.model.entity.Member;
import com.dama.service.ItemService;
import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
    public static boolean LoginToQr=false;
    public static String QR_LOGIN_USERNAME;

    private final MemberService memberService;

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

    @PostMapping("/api/camera/qr")
    public void qrCamera(@RequestBody QRLoginResponseDto qrLoginResponseDto){
        QR_LOGIN_USERNAME=qrLoginResponseDto.getUsername();
        LoginToQr=true;
    }

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
        }else if (!ItemState&&itemCode!=null&&!hashMap.isEmpty()){
            System.out.println("개수빠짐");
            hashMap.put(itemCode,hashMap.get(itemCode)-1);
        }

//        System.out.println("itemCode = " + itemCode);
        if (itemCode!=null){
            ResponseEntity<?> returnRespEntity = itemService.findItemStateByItemCodeToWebSocket(itemCode,hashMap.get(itemCode));
//            System.out.println("returnDto = " +returnRespEntity.getStatusCode());
            template.convertAndSend("/sub/chat/read/"+username,returnRespEntity);
        }
//        System.out.println("username = " + username);
        itemCode=null;
        ItemState=true;
    }

    @MessageMapping("/api/camera/qrLogin")
    public void itIsQrLogin() throws InterruptedException
    {
        if(LoginToQr){
            QRLoginStompResponseDTO qrLoginStompResponseDTO=new QRLoginStompResponseDTO();
            ResponseEntity<QRLoginStompResponseDTO> returnDTO = setQRLoginStomp(qrLoginStompResponseDTO, QR_LOGIN_USERNAME);
            template.convertAndSend("/sub/is/qrLogin",returnDTO);
        }else {
            QRLoginStompResponseDTO qrLoginStompResponseDTO=new QRLoginStompResponseDTO();
            ResponseEntity<QRLoginStompResponseDTO> returnDTO = setQRLoginStompNull(qrLoginStompResponseDTO);
            template.convertAndSend("/sub/is/qrLogin",returnDTO);
        }
        LoginToQr=false;
        QR_LOGIN_USERNAME="";
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
    public int returnTotalCount(){
        return totalCount;
    }
    public ResponseEntity<QRLoginStompResponseDTO> setQRLoginStomp(QRLoginStompResponseDTO qrLoginStompResponseDTO,String username){
        Member findMember = memberService.findByUsername(QR_LOGIN_USERNAME);
        qrLoginStompResponseDTO.setEmail(findMember.getEmail());
        qrLoginStompResponseDTO.setAccessToken(findMember.getAccessToken());
        qrLoginStompResponseDTO.setRefreshToken(findMember.getRefreshToken());
        qrLoginStompResponseDTO.setUsername(username);
        qrLoginStompResponseDTO.setSocialType(findMember.getSocialType().toString());
        qrLoginStompResponseDTO.setLoginToQr(LoginToQr);
        return new ResponseEntity<>(qrLoginStompResponseDTO, HttpStatus.OK);
    }
    public ResponseEntity<QRLoginStompResponseDTO> setQRLoginStompNull(QRLoginStompResponseDTO qrLoginStompResponseDTO){
        qrLoginStompResponseDTO.setEmail(null);
        qrLoginStompResponseDTO.setAccessToken(null);
        qrLoginStompResponseDTO.setRefreshToken(null);
        qrLoginStompResponseDTO.setUsername(null);
        qrLoginStompResponseDTO.setSocialType(null);
        qrLoginStompResponseDTO.setLoginToQr(LoginToQr);
        return new ResponseEntity<>(qrLoginStompResponseDTO,HttpStatus.OK);
    }
}
