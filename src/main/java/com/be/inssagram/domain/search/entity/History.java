package com.be.inssagram.domain.search.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class History {
    private String searchedData;
    private String type;
}
