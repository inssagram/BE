package com.be.inssagram.domain.tag.service;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.dto.response.PostInfoResponse;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.tag.dto.request.TagCreateRequest;
import com.be.inssagram.domain.tag.dto.response.TagInfoResponse;
import com.be.inssagram.domain.tag.entity.Tag;
import com.be.inssagram.domain.tag.repository.TagRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public TagInfoResponse createTag(TagCreateRequest request) {

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(UserDoesNotExistException::new);
        Post post = postRepository.findById(request.postId())
                .orElseThrow(PostDoesNotExistException::new);

        Tag tag = Tag.builder().member(member).post(post).build();

        return TagInfoResponse.from(tagRepository.save(tag));
    }

    public List<PostInfoResponse> searchPostWithMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(UserDoesNotExistException::new);

        return tagRepository
                .findByMemberIdAndAndImageId(memberId, null).stream()
                .map(Tag::getPost)
                .map(PostInfoResponse::from)
                .toList();
    }


}
