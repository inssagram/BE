package com.be.inssagram.domain.elastic.documents.index;

import com.be.inssagram.common.Indices;
import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.member.entity.Member;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Mapping(mappingPath = "elastic/hashtag-mapping.json")
@Document(indexName = Indices.HASHTAG_INDEX)
public class HashtagIndex {
    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    public static HashtagIndex from(HashTag hashTag) {
        return HashtagIndex.builder()
                .id(hashTag.getId())
                .name(hashTag.getName())
                .build();
    }
}
