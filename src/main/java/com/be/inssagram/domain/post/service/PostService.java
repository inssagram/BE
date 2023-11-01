package com.be.inssagram.domain.post.service;

import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.hashTag.service.HashTagService;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.tag.dto.request.TagCreateRequest;
import com.be.inssagram.domain.tag.entity.Tag;
import com.be.inssagram.domain.tag.repository.TagRepository;
import com.be.inssagram.domain.tag.service.TagService;
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
    private final TagRepository tagRepository;

    private final HashTagService hashTagService;
    private final TagService tagService;


    public PostInfoResponse createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .memberId(request.getMemberId())
                .image(request.getImage())
                .contents(request.getContents())
                .location(request.getLocation())
                .build();

        Set<Long> taggedMemberIds = new HashSet<>();
        List<String> hashTags = new ArrayList<>();

        PostInfoResponse response = PostInfoResponse.from(
                postRepository.save(post));


        // 해쉬 태그 저장
        if (request.getHashTags() != null) {
            for (String hashTag : request.getHashTags()) {
                hashTagService.saveHashTag(post, hashTag);
                hashTags.add(hashTag);
            }
            response.setHashTags(hashTags);
        }


        // 태그한 사람 저장
        if (request.getTaggedMemberIds() != null) {
            for (Long memberId : request.getTaggedMemberIds()) {
                if(memberRepository.existsById(memberId)){
                    tagService.createTag(TagCreateRequest.builder()
                            .postId(post.getId())
                            .memberId(memberId)
                            .build());
                    taggedMemberIds.add(memberId);
                }
            }
            response.setTaggedMemberIds(taggedMemberIds);
        }

        return response;
    }

    public PostInfoResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);
        post.updateFields(request);
        PostInfoResponse response = PostInfoResponse.from(post);


        // 해시 태그 수정
        Set<String> curHashTags = hashTagRepository.findByPost(post)
                .stream().map(HashTag::getName).collect(Collectors.toSet());
        Set<String> newHashTags = curHashTags;
        if (request.getHashTags() != null) {
            newHashTags = new HashSet<>(request.getHashTags());
        }
        updateHashTags(post, curHashTags, newHashTags);
        response.setHashTags(newHashTags.stream().toList());


        // 태그한 사람 수정
        Set<Long> curTaggedMember = tagRepository.findByPostIdAndImageId(
                post.getId(), null)
                .stream().map(Tag::getMember).map(Member::getId)
                .collect(Collectors.toSet());
        Set<Long> newTaggedMemberIds = curTaggedMember;
        if(request.getTaggedMemberIds() != null) {
            newTaggedMemberIds = new HashSet<>(request.getTaggedMemberIds());
        }
        updateTaggedMembers(post, curTaggedMember, newTaggedMemberIds);
        response.setTaggedMemberIds(new HashSet<>(newTaggedMemberIds));


        postRepository.save(post);
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

    private void updateHashTags(Post post, Set<String> curHashTags,
                                Set<String> newHashTags) {
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

    private void updateTaggedMembers(Post post, Set<Long> curTaggedMemberIds,
                                     Set<Long> newTaggedMemberIds) {
        if (!curTaggedMemberIds.equals(newTaggedMemberIds)) {
            curTaggedMemberIds.removeAll(newTaggedMemberIds);
            for (Long taggedMemberId : curTaggedMemberIds) {
                Tag tag = tagRepository.findByPostIdAndMemberIdAndImageId(
                        post.getId(), taggedMemberId, null);
                tagRepository.delete(tag);
            }

            for (Long newTaggedMemberId : newTaggedMemberIds) {
                if (tagRepository.findByPostIdAndMemberIdAndImageId(
                        post.getId(), newTaggedMemberId,null) != null) {
                    continue;
                }
                tagService.createTag(TagCreateRequest.builder()
                        .postId(post.getId())
                        .memberId(newTaggedMemberId)
                        .build());
            }
        }
    }

}