package com.be.inssagram.domain.like.repository;

import com.be.inssagram.domain.like.entity.Like;
import com.be.inssagram.domain.like.type.LikeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberIdAndLikeTypeAndAndLikeTypeId(
            Long memberId, LikeType type, Long typeId);

    List<Like> findByLikeTypeAndLikeTypeId(LikeType type, Long typeId);
}
