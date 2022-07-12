package com.dama.controller.api;

import com.dama.model.dto.BeaconDto;
import com.dama.model.dto.response.ItemListResponseDto;
import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StompController {

    private final SimpMessagingTemplate template;


    @PostMapping("/api/websocket/itemState")
    public static BeaconDto state(@RequestParam("ob_name") String ob_name,@RequestParam("beacon")String beacon){
        BeaconDto dto=new BeaconDto();
        dto.setBeacon(beacon);
        dto.setOb_name(ob_name);

        return dto;
    }


    @MessageMapping("/api/websocket/itemList")
    public void enter(@Payload BeaconDto beaconDto) {
        System.out.println("ob_name = " + beaconDto.getBeacon());
        template.convertAndSend("/sub/chat/read/",beaconDto);
    }
}
