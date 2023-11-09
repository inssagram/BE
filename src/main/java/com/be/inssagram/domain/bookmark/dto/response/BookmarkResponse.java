package com.be.inssagram.domain.bookmark.dto.response;

import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;


@Builder
@Getter
public class BookmarkResponse {
    private Long postId;
    private Long memberId;
    private String nickname;
    private List<String> image;
    private String contents;
    private String location;
    private Integer likeCount;
    private Integer commentsCounts;
    private Set<Long> taggedMemberIds;
    private List<String> hashTags;
    private String createdAt;

    public static BookmarkResponse from(Post post) {
        int commentCounts = 0;
        if (post.getComments() != null) {
            commentCounts = post.getComments().size();
        }
        return BookmarkResponse.builder()
                .postId(post.getId())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .image(post.getImage())
                .contents(post.getContents())
                .location(post.getLocation())
                .commentsCounts(commentCounts)
                .createdAt(post.getCreatedAt())
                .build();
    }
}
