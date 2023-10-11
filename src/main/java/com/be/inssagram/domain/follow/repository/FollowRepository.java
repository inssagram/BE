package com.be.inssagram.domain.follow.repository;

import com.be.inssagram.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByMyIdAndFollowId(String myId, String followId);

    Follow findByMyIdAndHashtagId(String myId, String hashtagId);
}
