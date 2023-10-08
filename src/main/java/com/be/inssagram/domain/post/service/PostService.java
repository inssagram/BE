package com.be.inssagram.domain.post.service;

import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
import com.be.inssagram.domain.taggedMember.repository.TaggedMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TaggedMemberRepository taggedMemberRepository;
    private final HashTagRepository hashTagRepository;

    public PostInfoResponse createPost(CreatePostRequest request) {

        Post post = Post.builder()
                .memberId(request.getMemberId())
                .image(request.getImage())
                .contents(request.getContents())
                .location(request.getLocation())
                .build();

        Set<TaggedMember> taggedMembers = new HashSet<>();
        Set<HashTag> hashTags = new HashSet<>();

        if (request.getTaggedMemberIds() != null) {
            List<TaggedMember> taggedMemberEntities =
                    taggedMemberRepository.findAllById(request.getTaggedMemberIds());
            taggedMembers.addAll(taggedMemberEntities);

//            for (Long id : request.getTaggedMemberIds()) {
//                if(taggedMemberRepository.existsById(id)) {
//                    taggedMembers.add(taggedMemberRepository.findById(id)
//                            .orElse(null));
//                }
//            }
        }

        if (request.getHashTagIds() != null) {
            List<HashTag> hashTagEntities =
                    hashTagRepository.findAllById(request.getHashTagIds());
            hashTags.addAll(hashTagEntities);

//            for (Long id : request.getHashTagIds()) {
//                if(hashTagRepository.existsById(id)) {
//                    hashTags.add(hashTagRepository.findById(id)
//                            .orElse(null));
//                }
//            }
        }
        post.setTaggedMembers(taggedMembers);
        post.setHashTags(hashTags);

        return PostInfoResponse.from(postRepository.save(post));
    }

    public PostInfoResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).
                orElseThrow(() -> new RuntimeException());
        post.updateFields(request);
        postRepository.save(post);
        return PostInfoResponse.from(post);
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("")
        );
        postRepository.save(post);  // 테스트 코드에서 확인 위한 작업.
        postRepository.delete(post);
    }

    public PostInfoResponse searchPostDetail(Long postId) {
        return postRepository.findById(postId)
                .map(PostInfoResponse::from)
                .orElseThrow(() -> new RuntimeException(""));
    }

    public Page<PostInfoResponse> searchPostAll(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostInfoResponse::from);
    }
}
