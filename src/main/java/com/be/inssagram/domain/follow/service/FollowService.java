package com.be.inssagram.domain.follow.service;

import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.hashTag.entity.HashTag;
import com.be.inssagram.domain.hashTag.repository.HashTagRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.exception.common.DataDoesNotExistException;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final HashTagRepository hashTagRepository;

    //팔로잉 및 해제
    public String follow(Member myInfo, FollowRequest request){
        Member memberInfo = memberRepository.findById(request.getFollowId()).orElseThrow(
                UserDoesNotExistException::new);
        Follow exists = followRepository.findByRequesterInfoAndFollowingInfo(myInfo, memberInfo);
        if(exists != null){
            followRepository.delete(exists);
            return "팔로잉을 해지하셧습니다";
        } else {
            if(request.getHashtagId() == null) {
                Follow follow = setFollowMember(myInfo, memberInfo);
                followRepository.save(follow);
                notificationService.notify(notificationService
                        .createNotifyDto(
                                memberInfo,
                                null,
                                myInfo,
                                myInfo.getNickname()+"님이 회원님을 팔로우 하셧습니다"
                        ));
                return "팔로잉 하셧습니다";
            }
            HashTag hashTag = hashTagRepository.findById(request.getHashtagId())
                    .orElseThrow(DataDoesNotExistException::new);
            followRepository.save(setFollowHashtag(myInfo, hashTag));
            return "팔로잉 하셧습니다";
        }
    }


    private Follow setFollowMember(Member myInfo, Member memberInfo){
        return Follow.builder()
                .requesterInfo(myInfo)
                .followingInfo(memberInfo)
                .build();
    }

    private Follow setFollowHashtag(Member myInfo, HashTag hashtagId){
        return Follow.builder()
                .requesterInfo(myInfo)
                .hashtagId(hashtagId)
                .build();
    }
}
