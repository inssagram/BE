package com.be.inssagram.domain.post.service;

import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.tag.repository.TagRepository;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    @Mock
    MemberRepository memberRepository;

    @Mock
    private LikeRepository likeRepository;
    @Mock
    private HashTagRepository hashTagRepository;
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void successCreatePost() {
        //given
        Post post = Post.builder()
                .id(1L)
//                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .comments(new ArrayList<>())
//                .taggedMembers(new HashSet<>())
                .build();
        given(postRepository.save(any())).willReturn(post);
        //when
        PostInfoResponse createResponse = postService.createPost(CreatePostRequest
                .builder()
                .memberId(2L)
                .image(new ArrayList<>())
                .contents("AAA")
                .location("sweet")
                .taggedMemberIds(new HashSet<>())
                .hashTags(new ArrayList<>())
                .build());
        //then
        assertEquals(1L, createResponse.getMemberId());
    }

    @Test
    void successUpdatePost() {
        //given
        Post post = Post.builder()
                .id(1L)
//                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .comments(new ArrayList<>())
//                .taggedMembers(new HashSet<>())
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        //when
        PostInfoResponse updateResponse = postService.updatePost(1L, UpdatePostRequest
                .builder()
                .contents("AAA")
                .location("sweet")
//                .taggedMembers(new HashSet<>())
                .hashTags(new ArrayList<>())
                .build());
        updateResponse.setHashTags(new ArrayList<>());
        //then
        assertEquals(1L, updateResponse.getMemberId());
        assertEquals("AAA", updateResponse.getContents());
    }

    @Test
    void successDeletePost() {
        //given
        Post post = Post.builder()
                .id(1L)
//                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .comments(new ArrayList<>())
//                .taggedMembers(new HashSet<>())
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
//                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .comments(new ArrayList<>())
//                .taggedMembers(new HashSet<>())
                .build();
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        //when
        PostInfoResponse searchResponse = postService.searchPostDetail(1L);

        searchResponse.setLikeCount(0);
        searchResponse.setHashTags(new ArrayList<>());
        //then
        assertEquals(1L, searchResponse.getMemberId());
    }

    @Test
    void successSearchPost_all() {
        //given
        Post post = Post.builder()
                .id(1L)
//                .memberId(1L)
                .image(new ArrayList<>())
                .contents("contents")
                .location("home")
                .comments(new ArrayList<>())
//                .taggedMembers(new HashSet<>())
                .build();
        List<Post> list = Collections.singletonList(post);
        given(postRepository.findAll()).willReturn(list);
        //when
        List<PostInfoResponse> responses = postService.searchPostAll();
        //then
        assertEquals(1L, responses.get(0).getPostId());
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
//                        .taggedMembers(new HashSet<>())
                        .hashTags(new ArrayList<>())
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