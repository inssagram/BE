package com.be.inssagram.domain.bookmark.entity;


import com.be.inssagram.domain.bookmark.dto.request.BookmarkRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long postId;

    public static Bookmark from(BookmarkRequest request, Long memberId){
        return Bookmark.builder()
                .memberId(memberId)
                .postId(request.getPostId())
                .build();
    }
}
