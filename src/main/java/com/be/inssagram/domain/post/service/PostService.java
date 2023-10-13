package com.be.inssagram.domain.post.service;

import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.hashTag.service.HashTagService;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final HashTagRepository hashTagRepository;

    private final HashTagService hashTagService;


    public PostInfoResponse createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .memberId(request.getMemberId())
                .image(request.getImage())
                .contents(request.getContents())
                .location(request.getLocation())
                .build();

        Set<String> taggedMembers = new HashSet<>();
        List<String> hashTags = new ArrayList<>();


        PostInfoResponse response = PostInfoResponse.from(
                postRepository.save(post));

        for (String hashTag : request.getHashTags()) {
            hashTagService.saveHashTag(post, hashTag);
            hashTags.add(hashTag);
        }

        post.setTaggedMembers(taggedMembers);
        response.setHashTags(hashTags);
        return response;
    }

    public PostInfoResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);
        post.updateFields(request);

        Set<String> curHashTags = hashTagRepository.findByPost(post)
                .stream().map(HashTag::getName).collect(Collectors.toSet());
        Set<String> newHashTags = new HashSet<>();

        if (request.getHashTags() != null) {
            newHashTags = new HashSet<>(request.getHashTags());
        }

        updateHashTags(post, curHashTags, newHashTags);

        postRepository.save(post);
        PostInfoResponse response = PostInfoResponse.from(post);
        response.setHashTags(newHashTags.stream().toList());
        return response;
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
        insertHashTags(post, response);
        insertLikeInfo(post, response);
        return response;
    }

    public List<PostInfoResponse> searchPostAll() {
        try {
            List<Post> posts = postRepository.findAll();
            return getPostInfoResponsesWithLikeInfo(posts);
        } catch (Exception e) {
            // 예외 처리: findAll 메서드에서 예외가 발생하면 빈 Page 객체를 반환
            return Collections.emptyList();
        }
    }

    public List<PostInfoResponse> searchPostWithMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new UserDoesNotExistException();
        }

        List<Post> posts = postRepository.findByMemberId(memberId);

        return getPostInfoResponsesWithLikeInfo(posts);
    }


    // 내부 메서드
    private List<PostInfoResponse> getPostInfoResponsesWithLikeInfo(List<Post> posts) {
        List<PostInfoResponse> responseList = posts.stream()
                .map(PostInfoResponse::from).toList();
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            PostInfoResponse response = responseList.get(i);
            insertLikeInfo(post, response);
            insertHashTags(post, response);
        }
        return responseList;
    }

    private void insertLikeInfo(Post post, PostInfoResponse response) {
        response.setLikeCount(likeRepository
                .findByPostAndCommentId(post, null).size());
    }

    private void updateHashTags(Post post, Set<String> curHashTags, Set<String> newHashTags) {
        if (!curHashTags.equals(newHashTags)) {
            curHashTags.removeAll(newHashTags);
            for (String curHashTag : curHashTags) {
                HashTag hashTag = hashTagRepository.findByPostAndName(post, curHashTag);
                hashTagRepository.delete(hashTag);
            }

            for (String newHashTag : newHashTags) {
                hashTagService.saveHashTag(post, newHashTag);
            }
        }
    }

    private void insertHashTags(Post post, PostInfoResponse response) {
        List<String> hashTags = hashTagRepository.findByPost(post).stream()
                .map(HashTag::getName).toList();
        response.setHashTags(hashTags);
    }
}