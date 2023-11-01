package com.be.inssagram.domain.elastic.documents.index;

import com.be.inssagram.common.Indices;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Mapping(mappingPath = "elastic/history-mapping.json")
@Document(indexName = Indices.HISTORY_INDEX)
public class History {
    @Id
    @Field(name = "created_at", type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(name = "member_id", type = FieldType.Long)
    private Long memberId;

    @Field(name = "searched", type = FieldType.Text)
    private String searched;
}
