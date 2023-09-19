package com.be.inssagram.domain.post.entity;

import com.be.inssagram.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Map;


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
    private Map<String, String> taggedMember;
    private String location;
    private Integer LikeCount;
    private Map<String, String> hashTag;




}
