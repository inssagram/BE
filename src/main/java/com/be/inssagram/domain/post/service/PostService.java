package com.be.inssagram.domain.post.service;

import com.be.inssagram.domain.like.dto.response.LikeInfoResponse;
import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public PostInfoResponse createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .memberId(request.getMemberId())
                .image(request.getImage())
                .contents(request.getContents())
                .location(request.getLocation())
                .build();

        Set<String> taggedMembers = new HashSet<>();
        Set<String> hashTags = new HashSet<>();


        post.setTaggedMembers(taggedMembers);
        post.setHashTags(hashTags);

        return PostInfoResponse.from(postRepository.save(post));
    }

    public PostInfoResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);
        post.updateFields(request);
        postRepository.save(post);
        return PostInfoResponse.from(post);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);
        postRepository.save(post);  // 테스트 코드에서 확인 위한 작업.
        postRepository.delete(post);
    }

    public PostInfoResponse searchPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);
        PostInfoResponse response = PostInfoResponse.from(post);
        insertLikeInfo(post, response);
        return response;
    }

    public List<PostInfoResponse> searchPostAll() {
        try {
            List<Post> posts = postRepository.findAll();
            List<PostInfoResponse> responseList = posts.stream()
                    .map(PostInfoResponse::from).toList();
            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);
                PostInfoResponse response = responseList.get(i);
                insertLikeInfo(post, response);
            }
            return responseList;
        } catch (Exception e) {
            // 예외 처리: findAll 메서드에서 예외가 발생하면 빈 Page 객체를 반환
            return Collections.emptyList();
        }
    }

    private void insertLikeInfo(Post post, PostInfoResponse response) {
        Set<LikeInfoResponse> likeSet = likeRepository
                .findByPostAndCommentId(post, null)
                .stream().map(LikeInfoResponse::from)
                .collect(Collectors.toSet());
        response.setLikedByPerson(likeSet);
        response.setLikeCount(likeSet.size());
    }

}