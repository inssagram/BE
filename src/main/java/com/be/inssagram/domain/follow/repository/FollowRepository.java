package com.be.inssagram.domain.follow.repository;

import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByRequesterInfoAndFollowingInfo(Member requesterInfo, Member memberInfo);
    Optional<Follow> findByRequesterInfoIdAndFollowingInfoId(Long requesterInfoId, Long memberInfoId);

    List<Follow> findAllByFollowingInfo(Member memberInfo);

    List<Follow> findAllByRequesterInfo(Member requesterInfo);
    List<Follow> findAllByRequesterInfoId(Long requesterId);

    Follow findByRequesterInfoAndHashtagName(Member myInfo, String hashtagName);
}
