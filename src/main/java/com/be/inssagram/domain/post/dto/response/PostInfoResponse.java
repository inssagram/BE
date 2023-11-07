package com.be.inssagram.domain.post.dto.response;

import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.type.PostType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInfoResponse {
    @Enumerated(EnumType.STRING)
    private PostType type;
    private Long postId;
    private Long memberId;
    private String nickName;
    private String memberImage;
    private List<String> image;
    private String contents;
    private String location;
    private Integer likeCount;
    private Integer commentsCounts;
    private Set<Long> taggedMemberIds;
    private List<String> hashTags;
    private String createdAt;

    public static PostInfoResponse from(Post post) {
        int commentCounts = 0;
        if (post.getComments() != null) {
            commentCounts = post.getComments().size();
        }
        return PostInfoResponse.builder()
                .postId(post.getId())
                .memberId(post.getMember().getId())
                .nickName(post.getMember().getNickname())
                .memberImage(post.getMember().getImage())
                .type(post.getType())
                .image(post.getImage())
                .contents(post.getContents())
                .location(post.getLocation())
                .commentsCounts(commentCounts)
                .createdAt(post.getCreatedAt())
                .build();
    }

}