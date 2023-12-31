package com.be.inssagram.domain.stroy.repository;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.stroy.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Boolean existsByMember(Member member);

    Optional<Story> findByMemberIdAndParentFlag(Long memberId, Boolean parentFlag);

    Boolean existsByMemberIdAndParentFlag(Long memberId, Boolean parentFlag);

    @Query("SELECT s FROM STORY s WHERE s.createdAt <= :twentyFourHoursAgo")
    List<Story> findStoriesCreatedBefore(String twentyFourHoursAgo);

}
