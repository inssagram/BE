package com.be.inssagram.domain.post.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.comment.entity.Comment;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
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
    @ElementCollection
    private List<String> likedByPerson;
    private Integer likeCount;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();
    @ElementCollection
    private Set<String> taggedMembers;
    @ElementCollection
    private Set<String> hashTags;


    public void setTaggedMembers(Set taggedMembers) {
        this.taggedMembers = taggedMembers;
    }

    public void setHashTags(Set hashTags) {
        this.hashTags = hashTags;
    }

    public void updateFields(UpdatePostRequest request) {
        if (request.getLocation() != null) {
            location = request.getLocation();
        }
        if (request.getContents() != null) {
            contents = request.getContents();
        }
        if (request.getTaggedMembers() != null) {
            taggedMembers = request.getTaggedMembers();
        }
        if (request.getHashTags() != null) {
            hashTags = request.getHashTags();
        }
    }

}