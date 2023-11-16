package com.be.inssagram.domain.chat.chatRoom.dto.response;

import com.be.inssagram.domain.member.entity.Member;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomUserInfo {
    private Long memberId;
    private String memberNickname;
    private String memberProfile;
    private Long memberFollowerCounts;
    private Long memberPostCounts;
    private boolean memberFollowState;

    public static ChatRoomUserInfo from(Member member) {
        return ChatRoomUserInfo.builder()
                .memberId(member.getId())
                .memberNickname(member.getNickname())
                .memberProfile(member.getImage())
                .build();
    }
}
