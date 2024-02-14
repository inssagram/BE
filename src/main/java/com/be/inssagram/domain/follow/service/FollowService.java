package com.be.inssagram.domain.follow.service;

import com.be.inssagram.domain.elastic.documents.index.HashtagIndex;
import com.be.inssagram.domain.elastic.documents.repository.HashtagSearchRepository;
import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.notification.service.NotificationService;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final HashtagSearchRepository hashtagSearchRepository;

    //팔로잉 및 해제
    public String follow(Member myInfo, FollowRequest request) {
        if (request.getFollowId() == null && request.getHashtagName() != null) {
            //db에 있는 정보인지 확인
            HashtagIndex hashTag = hashtagSearchRepository.findByName(request.getHashtagName());
                if(hashTag != null){
                    //팔로우 상태 확인
                    Follow exists = followRepository.findByRequesterInfoAndHashtagName(myInfo, hashTag.getName());
                    //팔로우일 경우 팔로우 해지
                    if (exists != null) {
                        followRepository.delete(exists);
                        return "팔로잉을 해지하셧습니다";
                    }
                    followRepository.save(setFollowHashtag(myInfo, hashTag.getName()));
                    return "팔로잉을 시작하셧습니다";
                }
            return "정보가 없습니다! 다시 확인 부탁드립니다";
        }
        Member memberInfo = memberRepository.findById(request.getFollowId()).orElseThrow(
                UserDoesNotExistException::new);
        Follow exists = followRepository.findByRequesterInfoAndFollowingInfo(myInfo, memberInfo);
        if (exists != null) {
            followRepository.delete(exists);
            notificationService.updateFriendStatus(myInfo, memberInfo);
            return "팔로잉을 해지하셧습니다";
        } else {
            if (request.getHashtagName() == null) {
                Follow follow = setFollowMember(myInfo, memberInfo);
                followRepository.save(follow);
                notificationService.notify(notificationService
                        .createNotifyDto(
                                memberInfo,
                                null,
                                myInfo,
                                myInfo.getNickname() + "님이 회원님을 팔로우하기 시작했습니다.",
                                null
                        ));
            }
        }
        return "팔로잉 하셧습니다";
    }

    public Page<InfoResponse> recommendFollowing(Member myInfo, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        //팔로우 상태가 아닌 회원 목록을 랜덤으로 추출후 InfoResponse 로 변환
        Page<InfoResponse> recommendations = followRepository.findRandomMembersNotFollowing(myInfo, pageable)
                .map(InfoResponse::fromEntity);
        //내 정보를 빼낸 리스트를 반환
        return recommendations.filter(infoResponse -> !infoResponse.member_id().equals(myInfo.getId()))
                .stream().collect(Collectors.collectingAndThen(Collectors.toList(), PageImpl::new));
    }

    private Follow setFollowMember(Member myInfo, Member memberInfo){
        return Follow.builder()
                .requesterInfo(myInfo)
                .followingInfo(memberInfo)
                .build();
    }

    private Follow setFollowHashtag(Member myInfo, String hashtagName){
        return Follow.builder()
                .requesterInfo(myInfo)
                .hashtagName(hashtagName)
                .build();
    }
}
