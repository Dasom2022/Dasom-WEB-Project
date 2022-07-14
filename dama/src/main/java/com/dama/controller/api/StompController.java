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
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Stack;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StompController {

    private final SimpMessagingTemplate template;

    private final ItemService itemService;

    private static String itemCode;


    @PostMapping("/api/websocket/state")
    public void state(@RequestBody BeaconDto beaconDto){
        System.out.println("ob_name = " + beaconDto.getItemCode());
        itemCode= beaconDto.getItemCode();
    }

    @MessageMapping("/api/websocket/itemList/{username}")
    public void enter(@DestinationVariable String username) throws InterruptedException {
        HashMap<String,Integer> hashMap=new HashMap<>();
        hashMap.put(itemCode,hashMap.getOrDefault(itemCode,0));
        ResponseEntity<?> returnRespEntity = itemService.findItemStateByItemCodeToWebSocket(itemCode,hashMap);
        System.out.println("username = " + username);
        System.out.println("returnDto = " +returnRespEntity.getStatusCode());
        template.convertAndSend("/sub/chat/read/"+username,returnRespEntity);
        itemCode=null;
    }
}
