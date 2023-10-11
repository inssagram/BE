package com.be.inssagram.domain.follow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String myId;

    private String followId;

    private String hashtagId;
}
