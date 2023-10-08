package com.be.inssagram.domain.post.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE inssagram.post SET deleted_at = current_timestamp WHERE post_id = ?")
@Where(clause = "deleted_at is NULL")
@Entity(name = "POST")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;
    private Long memberId;
    @ElementCollection
    private List<String> image;
    private String contents;
    private String location;
    @ManyToMany
    private List<Member> likedByPerson;
    private Integer likeCount;
    @OneToMany(mappedBy = "post") // 하나의 게시물은 여러 댓글을 가질 수 있습니다.
    private List<Comment> comments = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "POST_TAGGED_MEMBERS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "TAGGED_MEMBER_ID")
    )
    private Set<TaggedMember> taggedMembers;
    @ManyToMany
    @JoinTable(
            name = "POST_HASHTAGS",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "HASHTAG_ID")
    )
    private Set<HashTag> hashTags;


    public void setTaggedMembers(Set taggedMembers) {
        this.taggedMembers = taggedMembers;
    }

    public void setHashTags(Set hashTags) {
        this.hashTags = hashTags;
    }

    public void updateFields(UpdatePostRequest request) {
//        if (request.getImage() != null) {
//            image = request.getImage();
//        }
        if (request.getLocation() != null) {
            contents = request.getLocation();
        }
        if (request.getContents() != null) {
            location = request.getContents();
        }
        if (request.getTaggedMembers() != null) {
            taggedMembers = request.getTaggedMembers();
        }
        if (request.getHashTags() != null) {
            hashTags = request.getHashTags();
        }
    }

}
