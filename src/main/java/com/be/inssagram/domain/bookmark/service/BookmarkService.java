package com.be.inssagram.domain.bookmark.service;


import com.be.inssagram.domain.bookmark.dto.request.BookmarkRequest;
import com.be.inssagram.domain.bookmark.dto.response.BookmarkResponse;
import com.be.inssagram.domain.bookmark.entity.Bookmark;
import com.be.inssagram.domain.bookmark.repository.BookmarkRepository;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.common.DataDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    //게시물 북마크 기능
    public void bookmarkPost(BookmarkRequest request, Long memberId) {
        bookmarkRepository.save(Bookmark.from(request, memberId));
    }

    //게시물 북마크 해제
    public void removeBookmark(BookmarkRequest request, Long memberId) {
        Bookmark bookmark = bookmarkRepository.findByMemberIdAndPostId(memberId, request.getPostId())
                .orElseThrow(DataDoesNotExistException::new);
        bookmarkRepository.delete(bookmark);
    }

    //북마크한 게시물들 조회
    @Transactional
    public List<BookmarkResponse> getAllBookmarkPosts(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberId(memberId);

        List<BookmarkResponse> postList = bookmarks.stream()
                .map(bookmark -> postRepository.findById(bookmark.getPostId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(post -> {
                    // Access comments within the transaction
                    post.getComments().size(); // This line loads comments eagerly
                    return BookmarkResponse.from(post);
                })
                .collect(Collectors.toList());

        return postList;
    }
}
