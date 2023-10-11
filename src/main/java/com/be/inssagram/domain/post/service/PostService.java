package com.be.inssagram.domain.post.service;

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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

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
        return postRepository.findById(postId)
                .map(PostInfoResponse::from)
                .orElseThrow(PostDoesNotExistException::new);
    }

    public List<PostInfoResponse> searchPostAll() {
        try {
            return postRepository.findAll(pageable).map(PostInfoResponse::from);
        } catch (Exception e) {
            // 예외 처리: findAll 메서드에서 예외가 발생하면 빈 Page 객체를 반환
            return Collections.emptyList();
        }
    }

}