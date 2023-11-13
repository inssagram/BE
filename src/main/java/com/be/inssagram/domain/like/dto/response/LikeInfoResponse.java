package com.be.inssagram.domain.like.dto.response;

import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.like.type.LikeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeInfoResponse {
    private Long memberId;
    @Enumerated(EnumType.STRING)
    private LikeType likeType;
    private Long likeTypeId;

    public static LikeInfoResponse from(Like like) {
        return LikeInfoResponse.builder()
                .memberId(like.getMember().getId())
                .likeType(like.getLikeType())
                .likeTypeId(like.getLikeTypeId())
                .build();
    }
}
