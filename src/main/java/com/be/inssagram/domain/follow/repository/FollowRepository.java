package com.be.inssagram.domain.follow.repository;

import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByRequesterInfoAndFollowingInfo(Member requesterInfo, Member memberInfo);
    Optional<Follow> findByRequesterInfoIdAndFollowingInfoId(Long requesterInfoId, Long memberInfoId);
    Boolean existsByRequesterInfoIdAndFollowingInfoId(Long requesterInfo,Long memberInfoId);
    List<Follow> findAllByFollowingInfo(Member memberInfo);

    List<Follow> findAllByRequesterInfo(Member requesterInfo);
    List<Follow> findAllByRequesterInfoId(Long requesterId);

    @Query("SELECT m FROM Member m WHERE NOT EXISTS " +
            "(SELECT 1 FROM Follow f WHERE f.followingInfo = :followerInfo AND f.requesterInfo = m) " +
            "ORDER BY RAND()")
    Page<Member> findRandomMembersNotFollowing(@Param("followerInfo") Member followerInfo, Pageable pageable);

    Follow findByRequesterInfoAndHashtagName(Member myInfo, String hashtagName);
}
