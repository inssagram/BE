package com.be.inssagram.domain.post.dto.response;

import com.be.inssagram.domain.post.entity.Post;
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
    private Long postId;
    private Long memberId;
    private List<String> image;
    private String contents;
    private String location;
    private Integer likeCount;
    private Integer commentsCounts;
    private Set<Long> taggedMemberIds;
    private List<String> hashTags;

    public static PostInfoResponse from(Post post) {
        int commentCounts = 0;
        if (post.getComments() != null) {
            commentCounts = post.getComments().size();
        }
        return PostInfoResponse.builder()
                .postId(post.getId())
                .memberId(post.getMemberId())
                .image(post.getImage())
                .contents(post.getContents())
                .location(post.getLocation())
                .commentsCounts(commentCounts)
                .build();
    }

}