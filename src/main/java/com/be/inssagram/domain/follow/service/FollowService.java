package com.be.inssagram.domain.follow.service;

import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    //팔로잉 및 해제
    public String follow(InfoResponse myInfo, FollowRequest request){
        Member memberInfo = memberRepository.findById(request.getFollowId()).orElseThrow(
                UserDoesNotExistException::new);
        Follow exists = followRepository.findByMyIdAndMemberId(myInfo.member_id(), memberInfo.getId());
        if(exists != null){
            followRepository.delete(exists);
            return "팔로잉을 해지하셧습니다";
        } else {
            if(request.getHashtagId() == null) {
                Follow follow = setFollowMember(myInfo, memberInfo);
                followRepository.save(follow);
                notificationService.notify(notificationService
                        .createNotifyDto(
                                request.getFollowId(),
                                "follow",
                                follow.getId(),
                                myInfo.member_id(),
                                myInfo.nickname(),
                                myInfo.profilePic(),
                                myInfo.nickname()+"님이 회원님을 팔로우 하셧습니다"
                        ));
                return "팔로잉 하셧습니다";
            }
            followRepository.save(setFollowHashtag(myInfo.member_id(), request.getHashtagId()));
            return "팔로잉 하셧습니다";
        }
    }


    private Follow setFollowMember(InfoResponse myInfo, Member memberInfo){
        return Follow.builder()
                .myId(myInfo.member_id())
                .myName(myInfo.nickname())
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
