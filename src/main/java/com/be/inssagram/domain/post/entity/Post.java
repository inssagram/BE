package com.be.inssagram.domain.post.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE warendy.board SET deleted_at = current_timestamp WHERE board_id = ?")
@Where(clause = "deleted_at is NULL")
@Entity(name = "POST")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;
    private Long memberId;
    private String image;
    private String contents;
    private String location;
    private Integer LikeCount;

    @ManyToMany
    @JoinTable(
            name = "POST_TAGGED_MEMBERS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAGGED_MEMBER_ID")
    )
    private Set<TaggedMember> taggedMembers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "POST_HASHTAGS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "HASHTAG_ID")
    )
    private Set<HashTag> hashTags = new HashSet<>();

}
