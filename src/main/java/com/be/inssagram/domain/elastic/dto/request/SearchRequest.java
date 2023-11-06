package com.be.inssagram.domain.elastic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private Long memberId;
    private Long hashtagId;
}
