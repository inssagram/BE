package com.be.inssagram.domain.post.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.bookmark.entity.Bookmark;
import com.be.inssagram.domain.bookmark.repository.BookmarkRepository;
import com.be.inssagram.domain.firebase.service.FirebaseStorageService;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.hashTag.service.HashTagService;
import com.be.inssagram.domain.like.repository.LikeRepository;
import com.be.inssagram.domain.like.type.LikeType;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.dto.request.CreatePostRequest;
import com.be.inssagram.domain.post.dto.request.UpdatePostRequest;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.post.type.PostType;
import com.be.inssagram.domain.tag.dto.request.TagCreateRequest;
import com.be.inssagram.domain.tag.entity.Tag;
import com.be.inssagram.domain.tag.repository.TagRepository;
import com.be.inssagram.domain.tag.service.TagService;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BookmarkRepository bookmarkRepository;
    private final FollowRepository followRepository;

    private final HashTagService hashTagService;
    private final TagService tagService;
    private final FirebaseStorageService firebaseStorageService;

    private final TokenProvider tokenProvider;

    @Transactional
    public PostInfoResponse createPost(String token, CreatePostRequest request) {

        Member member = tokenProvider.getMemberFromToken(token);

        Post post = Post.builder()
                .member(member)
                .type(request.getType())
                .image(request.getImage())
                .fileNames(request.getFileName())
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
                if (memberRepository.existsById(memberId)) {
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

    @Transactional
    public PostInfoResponse updatePost(String token, Long postId, UpdatePostRequest request) {
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
        if (request.getTaggedMemberIds() != null) {
            newTaggedMemberIds = new HashSet<>(request.getTaggedMemberIds());
        }
        updateTaggedMembers(post, curTaggedMember, newTaggedMemberIds);
        response.setTaggedMemberIds(new HashSet<>(newTaggedMemberIds));

        Long memberId = tokenProvider.getMemberFromToken(token).getId();
        stateOfPostBookmarkAndFollow(postId, response, memberId);

        postRepository.save(post);
        return response;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);
        if (post.getFileNames() != null) {
            List<String> fileNames = post.getFileNames();
            for (String fileName : fileNames) {
                fileName = String.format("%s/%s/%s",
                        "post", post.getMember().getNickname(), fileName);
                firebaseStorageService.deleteFile(fileName);
            }
        } else {

        }
        postRepository.save(post);  // 테스트 코드에서 확인 위한 작업.
        postRepository.delete(post);
    }

    @Transactional
    public PostInfoResponse searchPostDetail(String token, Long postId) {
        Long memberId = tokenProvider.getMemberFromToken(token).getId();

        Post post = postRepository.findById(postId).orElseThrow(
                PostDoesNotExistException::new);

        PostInfoResponse response = PostInfoResponse.from(post);

        stateOfPostBookmarkAndFollow(postId, response, memberId);
        insertHashTags(post, response);
        insertLikeInfo(post, response);

        return response;
    }

    @Transactional
    public List<PostInfoResponse> searchPostAll(String token) {
        Long memberId = tokenProvider.getMemberFromToken(token).getId();

        List<Post> posts = postRepository.findByType(PostType.post);

        return getPostInfoResponses(memberId, posts);
    }

    @Transactional
    public List<PostInfoResponse> searchMyPosts(String token) {
        Long memberId = tokenProvider.getMemberFromToken(token).getId();

        List<Post> posts = postRepository.findByMemberIdAndType(
                memberId, PostType.post);

        return getPostInfoResponses(memberId, posts);
    }

    // 메인에서 내가 팔로우한 사람 + 내 포스트 리스트 조회
    @Transactional
    public List<PostInfoResponse> searchPostsWithFollowingMember(String token) {
        Long memberId = tokenProvider.getMemberFromToken(token).getId();

        List<Long> followingMemberIds = followRepository
                .findAllByRequesterInfoId(memberId).stream()
                .map(x -> x.getFollowingInfo().getId())
                .toList();

        // 팔로우하는 멤버들과 본인의 아이디를 포함하여 포스트를 한 번에 가져옴
        List<Long> allMemberIds = new ArrayList<>(followingMemberIds);
        allMemberIds.add(memberId);

        List<Post> posts = postRepository.findByMemberIdInAndType(allMemberIds, PostType.post);

        // 중복된 포스트 제거
        Set<Post> uniquePosts = new HashSet<>(posts);

        // 최종 결과 생성
        List<PostInfoResponse> result = getPostInfoResponses(memberId, new ArrayList<>(uniquePosts));

        return result.stream()
                .sorted(Comparator.comparing(PostInfoResponse::getCreatedAt)
                        .reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PostInfoResponse> searchPostWithMember(String token, Long memberId) {
        Long memberId2 = tokenProvider.getMemberFromToken(token).getId();
        if (!memberRepository.existsById(memberId)) {
            throw new UserDoesNotExistException();
        }
        List<Post> posts = postRepository.findByMemberIdAndType(
                memberId, PostType.post);

        return getPostInfoResponses(memberId2, posts);
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

    private List<PostInfoResponse> getPostInfoResponses(Long memberId2, List<Post> posts) {
        List<PostInfoResponse> responses =
                getPostInfoResponsesWithLikeInfo(posts);

        for (PostInfoResponse response : responses) {
            Long postId = response.getPostId();
            stateOfPostBookmarkAndFollow(postId, response, memberId2);
        }
        return responses;
    }

    private void insertLikeInfo(Post post, PostInfoResponse response) {
        response.setLikeCount(likeRepository
                .findByLikeTypeAndLikeTypeId(LikeType.post, post.getId()).size());
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
                        post.getId(), newTaggedMemberId, null) != null) {
                    continue;
                }
                if (memberRepository.existsById(newTaggedMemberId)) {
                    tagService.createTag(TagCreateRequest.builder()
                            .postId(post.getId())
                            .memberId(newTaggedMemberId)
                            .build());
                } else {
                    newTaggedMemberIds.remove(newTaggedMemberId);
                }
            }
        }
    }

    private void stateOfPostBookmarkAndFollow(Long postId, PostInfoResponse response, Long memberId) {
        if (likeRepository.findByMemberIdAndLikeTypeAndAndLikeTypeId(
                memberId, LikeType.post, postId).isPresent()) {
            response.setPostLike(true);
        }

        List<Bookmark> bookmarks = bookmarkRepository.findAllByMemberIdAndPostId(memberId, postId);
        if (!bookmarks.isEmpty()) {
            response.setBookmarked(true);
        }

        if (followRepository.findByRequesterInfoIdAndFollowingInfoId(
                memberId, response.getMemberId()).isPresent()) {
            response.setFollowed(true);
        }
    }

}