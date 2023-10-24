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
public class UpdatePostRequest {
    private String contents;
    private String location;
    private Set<String> taggedMembers;
    private List<String> hashTags;

}