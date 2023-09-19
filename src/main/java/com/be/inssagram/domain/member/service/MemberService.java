package com.be.inssagram.domain.member.service;


import com.be.inssagram.domain.member.dto.SignupRequest;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.DuplicatedUserException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup (SignupRequest request) {
        boolean existingMember = memberRepository.existsByEmail(request.getEmail());
        if(existingMember){
            throw new DuplicatedUserException();
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(setAccount(request));
    }

    private Member setAccount (SignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .gender(request.getGender())
                .jobField(request.getJobField())
                .build();
    }
}