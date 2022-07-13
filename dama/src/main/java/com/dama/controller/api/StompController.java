package com.dama.controller.api;

import com.dama.model.dto.BeaconDto;
import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.response.ItemListResponseDto;
import com.dama.model.entity.Member;
import com.dama.repository.ItemRepository;
import com.dama.repository.MemberRepository;
import com.dama.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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
        ResponseEntity<?> returnRespEntity = itemService.findItemStateByItemCodeToWebSocket(itemCode);
        System.out.println("username = " + username);
        System.out.println("returnDto = " +returnRespEntity.getStatusCode());
        template.convertAndSend("/sub/chat/read/"+username,returnRespEntity);
        itemCode=null;
    }
}
