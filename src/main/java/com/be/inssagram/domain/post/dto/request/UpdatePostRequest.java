package com.be.inssagram.domain.post.dto.request;


import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
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
//    private List<String> image;
    private String contents;
    private String location;
    private Set<TaggedMember> taggedMembers;
    private Set<HashTag> hashTags;

}
