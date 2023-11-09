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
public class UpdatePostRequest {
    @Enumerated(EnumType.STRING)
    private PostType type;
    private String contents;
    private String location;
    private Set<Long> taggedMemberIds;
    private List<String> hashTags;

}