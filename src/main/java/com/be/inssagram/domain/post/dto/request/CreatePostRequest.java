package com.be.inssagram.domain.post.dto.request;

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
public class CreatePostRequest {
    @Enumerated(EnumType.STRING)
    private PostType type;
    private List<String> image;
    private List<String> fileName;
    private String contents;
    private String location;
    private Set<Long> taggedMemberIds; // TaggedMember 목록
    private List<String> hashTags; // HashTag 목록

}