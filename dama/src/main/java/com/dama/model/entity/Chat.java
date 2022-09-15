package com.dama.model.entity;

import com.dama.model.dto.chat.ChatMessageDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String chatTime;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    /*@ManyToOne
    @JoinColumn(name = "member_id")*/
    private String writer;

    private String message;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Member> readMember=new ArrayList<>();

    @Column(name = "messageType")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Builder
    public Chat(Long id, String chatTime, ChatRoom chatRoom, String  writer, String message) {
        this.id = id;
        this.chatTime = chatTime;
        this.chatRoom = chatRoom;
        this.writer = writer;
        this.message = message;
    }

    public ChatMessageDto toDto(){
        return ChatMessageDto.builder()
                .id(id)
                .writer(writer)
                .message(message)
                .chatTime(chatTime)
                .messageType(messageType)
                .build();
    }

    public void toUpdateReadMember(Member member){
        this.readMember.add(member);
    }
}
