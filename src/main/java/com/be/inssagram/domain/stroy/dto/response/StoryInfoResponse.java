package com.be.inssagram.domain.stroy.dto.response;

import com.be.inssagram.domain.stroy.entity.Story;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryInfoResponse {
    private Long storyId;
    private Long memberId;
    private String memberNickname;
    private String memberProfile;
    private String image;
    private String createdAt;
    private String updatedAt;
    private String passedTime;

    public static StoryInfoResponse from(Story story) {
        return StoryInfoResponse.builder()
                .storyId(story.getId())
                .memberId(story.getMember().getId())
                .memberNickname(story.getMember().getNickname())
                .memberProfile(story.getMember().getImage())
                .image(story.getImages())
                .createdAt(story.getCreatedAt())
                .updatedAt(story.getUpdatedAt())
                .passedTime(story.getPassedTime())
                .build();
    }
}
