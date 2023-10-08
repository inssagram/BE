package com.be.inssagram.domain.taggedMember.repository;

import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedMemberRepository extends JpaRepository<TaggedMember, Long> {

    TaggedMember findByName(String newNickname);
}
