package com.dama.model.dto.chat;

import com.dama.model.entity.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class createChatRoomDto{
        private String roomCode;
        private String name;

        public ChatRoom toEntity(){
            return ChatRoom.builder()
                    .roomCode(roomCode)
                    .name(name)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class enterChatRoomDto{
        private Long id;
    }
}
