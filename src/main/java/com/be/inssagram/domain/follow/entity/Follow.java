package com.be.inssagram.domain.follow.entity;

import com.be.inssagram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;


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

    private Long myId;

    private String myName;

    private Long memberId;

    private String memberName;

    private Long hashtagId;
}
