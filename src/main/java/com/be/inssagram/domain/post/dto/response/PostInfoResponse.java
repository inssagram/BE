package com.be.inssagram.domain.post.dto.response;

import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostInfoResponse {

//    private Long postId;
//    private Long memberId;
    private List<String> image;
    private String contents;
    private String location;
    private Integer likeCount;
    private Set<Long> taggedMemberIds;
    private Set<Long> hashTagIds;

    public static PostInfoResponse from(Post post) {

        Set<Long> taggedMemberIds = post.getTaggedMembers().stream()
                .map(TaggedMember::getId)
                .collect(Collectors.toSet());

        Set<Long> hashTagIds = post.getHashTags().stream()
                .map(HashTag::getId)
                .collect(Collectors.toSet());

        return PostInfoResponse.builder()
//                .postId(post.getId())
//                .memberId(post.getMemberId())
                .image(post.getImage())
                .contents(post.getContents())
                .location(post.getLocation())
                .likeCount(post.getLikeCount())
                .taggedMemberIds(taggedMemberIds)
                .hashTagIds(hashTagIds)
                .build();
    }

}
