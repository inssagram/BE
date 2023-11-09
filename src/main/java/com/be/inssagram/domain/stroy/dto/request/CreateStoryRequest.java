package com.be.inssagram.domain.stroy.dto.request;

import lombok.*;

import java.util.Set;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStoryRequest {
    private String image;
    private String contents;
    private String location;
    private Set<Long> taggedMemberIds;
}
