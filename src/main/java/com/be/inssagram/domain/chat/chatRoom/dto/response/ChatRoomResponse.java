package com.be.inssagram.domain.chat.chatRoom.dto.response;

import com.be.inssagram.domain.chat.chatRoom.entity.ChatRoom;
import com.be.inssagram.domain.member.entity.Member;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long chatRoomId;

    private Long firstMemberId;
    private String firstMemberNickname;
    private String firstMemberProfile;
    private Long firstMemberFollowerCounts;
    private Long firstMemberPostCounts;
    private boolean firstMemberFollowState;

    private Long secondMemberId;
    private String secondMemberNickname;
    private String secondMemberProfile;
    private Long secondMemberFollowCounts;
    private Long secondMemberPostCounts;
    private boolean secondMemberFollowState;


    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getRoomId())
                .firstMemberId(chatRoom.getFirstParticipant().getId())
                .firstMemberNickname(chatRoom.getFirstParticipant().getNickname())
                .firstMemberProfile(chatRoom.getFirstParticipant().getImage())
                .secondMemberId(chatRoom.getSecondParticipant().getId())
                .secondMemberNickname(chatRoom.getSecondParticipant().getNickname())
                .secondMemberProfile(chatRoom.getSecondParticipant().getImage())
                .build();
    }

//    public static ChatRoomResponse createResponse(
//            Long chatRoomId, Member sender, Member receiver) {
//        return ChatRoomResponse.builder()
//                .chatRoomId(chatRoomId)
//                .firstMemberId(sender.getId())
//                .firstMemberNickname(sender.getNickname())
//                .firstMemberProfile(sender.getImage())
//                .secondMemberId(receiver.getId())
//                .secondMemberNickname(receiver.getNickname())
//                .secondMemberProfile(receiver.getImage())
//                .build();
//    }

}
