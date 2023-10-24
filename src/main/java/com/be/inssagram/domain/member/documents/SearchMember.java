package com.be.inssagram.domain.member.documents;

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
@Setting(settingPath = "elastic/member-setting.json")
@Mapping(mappingPath = "elastic/member-mapping.json")
@Document(indexName = Indices.MEMBER_INDEX)
public class SearchMember {
    @Id
    @Field(name = "member_id", type = FieldType.Long)
    private Long id;

    @Field(name = "member_email", type = FieldType.Text)
    private String email;

    @Field(name = "member_nickname", type = FieldType.Text)
    private String nickname;

    @Field(name = "member_company_name", type = FieldType.Text)
    private String companyName;

    public static SearchMember from(Member member) {
        return SearchMember.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .companyName(member.getCompanyName())
                .build();
    }
}
