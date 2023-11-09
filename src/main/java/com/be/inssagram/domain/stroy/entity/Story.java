package com.be.inssagram.domain.stroy.entity;

import com.be.inssagram.common.BaseEntity;
import com.be.inssagram.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "STORY")
public class Story extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORY_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;
    private String images;
    private String contents;
    private String location;
    private boolean parentFlag;
    @ManyToOne(fetch = FetchType.LAZY)
    private Story parentStory;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentStory", orphanRemoval = true)
    private List<Story> childStory = new LinkedList<>();
    private Long updatedFlag;
    private String passedTime;

    public void setPassedTime(String passedTime) {
        this.passedTime = passedTime;
    }
}
