package com.dama.controller.api;

import com.dama.model.dto.response.ItemListResponseDto;
import com.dama.model.entity.Member;
import com.dama.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StompController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/api/websocket/itemList")
    public void enter(ItemListResponseDto itemListResponseDto) {
        template.convertAndSend("/sub/chat/read/",itemListResponseDto.getItemName());
    }
}
