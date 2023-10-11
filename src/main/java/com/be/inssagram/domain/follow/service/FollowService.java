package com.be.inssagram.domain.follow.service;

import com.be.inssagram.domain.follow.dto.request.FollowRequest;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    //계정 팔로잉 및 해제
    public String followMember(FollowRequest request){
        Follow exists = followRepository.findByMyIdAndFollowId(request.getMyId(), request.getFollowId());
        if(exists != null){
            followRepository.delete(exists);
            return "팔로잉을 해지하셧습니다";
        } else {
            followRepository.save(setFollowMember(request.getMyId(), request.getFollowId()));
            return "팔로잉 하셧습니다";
        }
    }

    //해시태그 팔로잉 및 해제
    public String followHashtag(FollowRequest request){
        Follow exists = followRepository.findByMyIdAndHashtagId(request.getMyId(), request.getHashtagId());
        if(exists != null){
            followRepository.delete(exists);
            return "팔로잉을 해지하셧습니다";
        } else {
            followRepository.save(setFollowHashtag(request.getMyId(), request.getHashtagId()));
            return "팔로잉 하셧습니다";
        }
    }

    private Follow setFollowMember(String myId, String followerId){
        return Follow.builder()
                .myId(myId)
                .followId(followerId)
                .build();
    }

    private Follow setFollowHashtag(String myId, String hashtagId){
        return Follow.builder()
                .myId(myId)
                .hashtagId(hashtagId)
                .build();
    }
}
