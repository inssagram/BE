package com.be.inssagram.domain.elastic.documents.index;

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
@Mapping(mappingPath = "elastic/history-mapping.json")
@Document(indexName = Indices.HISTORY_INDEX)
public class HistoryIndex {

    @Id
    @Field(name = "id", type = FieldType.Long)
    private Long id;

    @Field(name = "created_at", type = FieldType.Text)
    private String createdAt;

    @Field(name = "member_id", type = FieldType.Long)
    private Long memberId;

    @Field(name = "job", type = FieldType.Text)
    private String job;

    @Field(name = "image", type = FieldType.Text)
    private String image;

    @Field(name = "search_id", type = FieldType.Long)
    private Long search_id;

    @Field(name = "searched", type = FieldType.Text)
    private String searched;
}
