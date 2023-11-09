package com.be.inssagram.domain.follow.entity;

import com.be.inssagram.domain.elastic.documents.index.HashtagIndex;
import com.be.inssagram.domain.hashTag.entity.HashTag;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private Member requesterInfo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member followingInfo;

    private String hashtagName;
}
