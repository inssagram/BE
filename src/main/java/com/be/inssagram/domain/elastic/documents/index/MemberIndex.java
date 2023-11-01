package com.be.inssagram.domain.elastic.documents.index;

import com.be.inssagram.common.Indices;
import com.be.inssagram.domain.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Mapping(mappingPath = "elastic/member-mapping.json")
@Document(indexName = Indices.MEMBER_INDEX)
public class MemberIndex {
    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;

    @Field(name = "email", type = FieldType.Text)
    private String email;

    @Field(name = "name", type = FieldType.Text)
    private String nickname;

    @Field(name = "company_name", type = FieldType.Text)
    private String companyName;

    public static MemberIndex from(Member member) {
        return MemberIndex.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .companyName(member.getCompanyName())
                .build();
    }
}
