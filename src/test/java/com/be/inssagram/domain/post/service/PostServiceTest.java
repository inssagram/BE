package com.be.inssagram.domain.post.service;

import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void successCreatePost() {
        //given
        Post post = Post.builder()
                .id(1L)
                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .likedByPerson(new ArrayList<>())
                .likeCount(34)
                .comments(new ArrayList<>())
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build();
        given(postRepository.save(any())).willReturn(post);
        //when
        PostInfoResponse createResponse = postService.createPost(CreatePostRequest
                .builder()
                .memberId(2L)
                .image(new ArrayList<>())
                .contents("AAA")
                .location("sweet")
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build());
        //then
        assertEquals(1L, createResponse.getMemberId());
    }

    @Test
    void successUpdatePost() {
        //given
        Post post = Post.builder()
                .id(1L)
                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .likedByPerson(new ArrayList<>())
                .likeCount(34)
                .comments(new ArrayList<>())
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        //when
        PostInfoResponse updateResponse = postService.updatePost(1L, UpdatePostRequest
                .builder()
                .contents("AAA")
                .location("sweet")
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build());
        //then
        assertEquals(1L, updateResponse.getMemberId());
        assertEquals("AAA", updateResponse.getContents());
    }

    @Test
    void successDeletePost() {
        //given
        Post post = Post.builder()
                .id(1L)
                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .likedByPerson(new ArrayList<>())
                .likeCount(34)
                .comments(new ArrayList<>())
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        //when
        postService.deletePost(1L);
        //then
        verify(postRepository, times(1)).save(captor.capture());
        assertEquals(1, captor.getValue().getId());
    }

    @Test
    void successSearchPost_detail() {
        //given
        Post post = Post.builder()
                .id(1L)
                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .likedByPerson(new ArrayList<>())
                .likeCount(34)
                .comments(new ArrayList<>())
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        //when
        PostInfoResponse searchResponse = postService.searchPostDetail(1L);
        //then
        assertEquals(1L, searchResponse.getMemberId());
    }

    @Test
    void successSearchPost_all() {
        //given
        Post post = Post.builder()
                .id(1L)
                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .likedByPerson(new ArrayList<>())
                .likeCount(34)
                .comments(new ArrayList<>())
                .taggedMembers(new HashSet<>())
                .hashTags(new HashSet<>())
                .build();
        List<Post> list = Collections.singletonList(post);
        Page<Post> page = new PageImpl<>(list);
        given(postRepository.findAll((Pageable) any())).willReturn(page);
        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<PostInfoResponse> responses = postService.searchPostAll(pageable);
        //then
        assertEquals(1L, responses.getContent().get(0).getPostId());
    }

    @Test
    void failedUpdatePost() {
        //given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(PostDoesNotExistException.class,
                () -> postService.updatePost(1L, UpdatePostRequest
                        .builder()
                        .contents("AAA")
                        .location("sweet")
                        .taggedMembers(new HashSet<>())
                        .hashTags(new HashSet<>())
                        .build()));
        //then
        assertEquals("존재하지 않는 게시글 입니다.", exception.getMessage());
    }

    @Test
    void failedDeletePost() {
        //given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(PostDoesNotExistException.class,
                () -> postService.deletePost(1L));
        //then
        assertEquals("존재하지 않는 게시글 입니다.", exception.getMessage());
    }

    @Test
    void failedSearchPostDetail() {
        //given
        given(postRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        RuntimeException exception = assertThrows(PostDoesNotExistException.class,
                () -> postService.searchPostDetail(1L));
        //then
        assertEquals("존재하지 않는 게시글 입니다.", exception.getMessage());
    }
}