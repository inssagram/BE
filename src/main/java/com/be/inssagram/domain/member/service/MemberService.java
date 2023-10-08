package com.be.inssagram.domain.member.service;

import com.be.inssagram.domain.member.dto.request.SignupRequest;
import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.listener.MemberUpdateEvent;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.*;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;


    public InfoResponse updateMember(Long id, UpdateRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
//        if (request.getPassword() != null) {
//            if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
//                throw new SamePasswordException();
//            }
//            request.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
        member.updateFields(request);
        memberRepository.save(member);
        eventPublisher.publishEvent(new MemberUpdateEvent(this, member));
        return InfoResponse.fromEntity(member);
    }

    private Member setAccount(SignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .gender(request.getGender())
                .jobField(request.getJobField())
                .build();
    }
}