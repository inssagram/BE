package com.be.inssagram.domain.follow.service;

import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    //팔로잉 및 해제
    public String follow(FollowRequest request){
        Member memberInfo = memberRepository.findById(request.getFollowId()).orElseThrow(
                UserDoesNotExistException::new);
        Follow exists = followRepository.findByMyIdAndMemberId(request.getMyId(), memberInfo.getId());
        if(exists != null){
            followRepository.delete(exists);
            return "팔로잉을 해지하셧습니다";
        } else {
            if(request.getHashtagId() == null) {
                followRepository.save(setFollowMember(request, memberInfo));
                return "팔로잉 하셧습니다";
            }
            followRepository.save(setFollowHashtag(request.getMyId(), request.getHashtagId()));
            return "팔로잉 하셧습니다";
        }
    }


    private Follow setFollowMember(FollowRequest myInfo, Member memberInfo){
        return Follow.builder()
                .myId(myInfo.getMyId())
                .myName(myInfo.getMyName())
                .memberId(memberInfo.getId())
                .memberName(memberInfo.getNickname())
                .build();
    }

    private Follow setFollowHashtag(Long myId, Long hashtagId){
        return Follow.builder()
                .myId(myId)
                .hashtagId(hashtagId)
                .build();
    }
}
