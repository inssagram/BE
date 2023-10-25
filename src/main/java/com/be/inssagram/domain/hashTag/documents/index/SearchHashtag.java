package com.be.inssagram.domain.hashTag.documents.index;

import com.be.inssagram.common.Indices;
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
public class SearchHashtag {
    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;

    @Field(name = "name", type = FieldType.Text)
    private String nickname;
}
