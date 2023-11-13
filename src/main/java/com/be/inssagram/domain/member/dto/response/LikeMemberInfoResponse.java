package com.be.inssagram.domain.member.dto.response;


import com.be.inssagram.domain.member.entity.Member;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeMemberInfoResponse {
    private Long memberId;
    private String memberNickname;
    private String memberProfile;
    private boolean followedState;

    public static LikeMemberInfoResponse from(Member member) {
        return LikeMemberInfoResponse.builder()
                .memberId(member.getId())
                .memberNickname(member.getNickname())
                .memberProfile(member.getImage())
                .build();
    }
}
