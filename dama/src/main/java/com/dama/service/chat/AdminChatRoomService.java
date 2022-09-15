package com.dama.service.chat;

import com.dama.model.entity.ChatRoom;
import com.dama.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ResponseEntity<?> getChatRoomList(){
        List<ChatRoom> allChatRoom = chatRoomRepository.findAll();
        if (allChatRoom.size()==0){
            return new ResponseEntity<>("아직 채팅방이 없습니다",HttpStatus.OK);
        }else {
            Collections.reverse(allChatRoom);
            return new ResponseEntity<>(allChatRoom, HttpStatus.OK);
        }
    }
}
