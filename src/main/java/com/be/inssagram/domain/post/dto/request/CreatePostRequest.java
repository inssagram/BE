package com.be.inssagram.domain.post.dto.request;

import lombok.*;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {
    private List<String> image;
    private String contents;
    private String location;
    private Set<Long> taggedMemberIds; // TaggedMember 목록
    private List<String> hashTags; // HashTag 목록

}