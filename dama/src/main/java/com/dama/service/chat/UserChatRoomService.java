package com.dama.service.chat;

import com.dama.model.dto.chat.ChatRoomDto;
import com.dama.model.entity.ChatRoom;
import com.dama.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ResponseEntity<?> createChatRoomToAdmin(ChatRoomDto.createChatRoomDto createChatRoomDto){
        Optional<ChatRoom> findRoom = chatRoomRepository.findByName(createChatRoomDto.getName());
        if (findRoom.isEmpty()){
            createChatRoomDto.setRoomCode(UUID.randomUUID().toString());
            ChatRoom ch = createChatRoomDto.toEntity();
            chatRoomRepository.save(ch);
            return new ResponseEntity<>(ch, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("이미 같은 이름의 방이 존재합니다.",HttpStatus.OK);
        }
    }

    @Transactional
    public ResponseEntity<?> enterChatRoomToUserRoom(ChatRoomDto.enterChatRoomDto enterChatRoomDto){
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findById(enterChatRoomDto.getId());
        return new ResponseEntity<>(findChatRoom.get(),HttpStatus.OK);
    }


}
