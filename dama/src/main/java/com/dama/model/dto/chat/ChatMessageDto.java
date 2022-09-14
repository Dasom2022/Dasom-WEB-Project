package com.dama.model.dto.chat;

import com.dama.model.entity.Chat;
import com.dama.model.entity.ChatRoom;
import com.dama.model.entity.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ChatMessageDto {

    private Long id;

    private String chatTime;

    private ChatRoom chatRoom;

    private String writer;

    private String message;

    private MessageType messageType;

    private int chatRoomUserCount;

    @Builder
    public ChatMessageDto(Long id, String writer, String message, String chatTime, ChatRoom chatRoom, MessageType messageType, int chatRoomUserCount){
        this.id=id;
        this.writer=writer;
        this.message=message;
        this.chatTime=chatTime;
        this.chatRoomUserCount=chatRoomUserCount;
        this.messageType=messageType;
    }

    public Chat toEntity(){
        return Chat.builder()
                .id(id)
                .chatTime(chatTime)
                .chatRoom(chatRoom)
                .message(message)
                .build();
    }
}
