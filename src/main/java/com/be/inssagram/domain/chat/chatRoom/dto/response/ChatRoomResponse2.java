package com.be.inssagram.domain.chat.chatRoom.dto.response;

import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse2 {
    private Long chatRoomId;

    private Map<Long, ChatRoomUserInfo> memberList;

    public static ChatRoomResponse2 from(ChatRoom chatRoom) {

        Map<Long, ChatRoomUserInfo> memberList = new HashMap<>();

        ChatRoomUserInfo firstMember = ChatRoomUserInfo
                .from(chatRoom.getFirstParticipant());
        ChatRoomUserInfo secondMember = ChatRoomUserInfo
                .from(chatRoom.getSecondParticipant());

        memberList.put(firstMember.getMemberId(), firstMember);
        memberList.put(secondMember.getMemberId(), secondMember);

        return ChatRoomResponse2.builder()
                .chatRoomId(chatRoom.getRoomId())
                .memberList(memberList)
                .build();
    }

}
