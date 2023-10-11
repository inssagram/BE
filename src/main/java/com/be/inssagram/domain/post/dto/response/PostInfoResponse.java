package com.be.inssagram.domain.post.dto.response;

import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.like.dto.response.LikeInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import lombok.*;

import java.util.ArrayList;
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
    private Set<LikeInfoResponse> likedByPerson;
    private Integer likeCount;
    private Integer commentsCounts;
    private Set<String> taggedMembers;
    private Set<String> hashTags;

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
//                .comments(post.getComments())
                .commentsCounts(commentCounts)
                .taggedMembers(post.getTaggedMembers())
                .hashTags(post.getHashTags())
                .build();
    }

}