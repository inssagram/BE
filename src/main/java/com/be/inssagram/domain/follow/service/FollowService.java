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

    //팔로잉 및 해제
    public String follow(FollowRequest request){
        Follow exists = followRepository.findByMyIdAndFollowId(request.getMyId(), request.getFollowId());
        if(exists != null){
            followRepository.delete(exists);
            return "팔로잉을 해지하셧습니다";
        } else {
            if(request.getHashtagId() == null) {
                followRepository.save(setFollowMember(request.getMyId(), request.getFollowId()));
            }
            followRepository.save(setFollowHashtag(request.getMyId(), request.getHashtagId()));
            return "팔로잉 하셧습니다";
        }
    }


    private Follow setFollowMember(Long myId, Long followId){
        return Follow.builder()
                .myId(myId)
                .followId(followId)
                .build();
    }

    private Follow setFollowHashtag(Long myId, Long hashtagId){
        return Follow.builder()
                .myId(myId)
                .hashtagId(hashtagId)
                .build();
    }
}
