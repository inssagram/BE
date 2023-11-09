package com.be.inssagram.domain.follow.repository;

import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByRequesterInfoAndFollowingInfo(Member requesterInfo, Member memberInfo);

    List<Follow> findAllByFollowingInfo(Member memberInfo);

    List<Follow> findAllByRequesterInfo(Member requesterInfo);

    Follow findByRequesterInfoAndHashtagName(Member myInfo, String hashtagName);
}
