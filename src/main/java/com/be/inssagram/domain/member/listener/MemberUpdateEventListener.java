package com.be.inssagram.domain.member.listener;

import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.taggedMember.entity.TaggedMember;
import com.be.inssagram.domain.taggedMember.repository.TaggedMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemberUpdateEventListener {

    private final TaggedMemberRepository taggedMemberRepository;

    @Autowired
    public MemberUpdateEventListener(TaggedMemberRepository taggedMemberRepository) {
        this.taggedMemberRepository = taggedMemberRepository;
    }

    @EventListener
    public void handleMemberUpdateEvent(MemberUpdateEvent event) {
        Member updatedMember = event.getUpdatedMember();

        // 이벤트에서 변경된 nickname을 가져와서 TaggedMember 엔티티의 name을 업데이트
        String newNickname = updatedMember.getNickname();
        TaggedMember taggedMember = taggedMemberRepository.findByName(newNickname);
        if (taggedMember != null) {
            taggedMember.setName(newNickname);
            taggedMemberRepository.save(taggedMember);
        }
    }
}

