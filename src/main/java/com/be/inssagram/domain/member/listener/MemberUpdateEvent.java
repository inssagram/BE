package com.be.inssagram.domain.member.listener;

import com.be.inssagram.domain.member.entity.Member;
import org.springframework.context.ApplicationEvent;

public class MemberUpdateEvent extends ApplicationEvent {
    private final Member updatedMember;

    public MemberUpdateEvent(Object source, Member updatedMember) {
        super(source);
        this.updatedMember = updatedMember;
    }

    public Member getUpdatedMember() {
        return updatedMember;
    }
}
