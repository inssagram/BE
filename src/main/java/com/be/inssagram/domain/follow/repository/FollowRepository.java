package com.be.inssagram.domain.follow.repository;

import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByMyIdAndMemberId(Long myId, Long MemberId);

    List<Follow> findAllByMyId(Long myId);

    List<Follow> findAllByMemberId(Long followId);

    Follow findByMyIdAndHashtagId(Long myId, Long hashtagId);
}
