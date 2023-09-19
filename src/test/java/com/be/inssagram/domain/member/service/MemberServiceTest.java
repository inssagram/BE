package com.be.inssagram.domain.member.service;

import com.be.inssagram.domain.member.dto.SigninRequest;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("signup")
    void successCreateMember() {
        //given
        Member newMember = Member.builder()
                .email("test@test.com")
                .jobField("programmer")
                .gender("female")
                .nickname("testNicks")
                .password("test123")
                .build();

        memberRepository.save(newMember);
        given(memberRepository.findByEmail(newMember.getEmail()))
                .willReturn(newMember);
        //when
        Member member = memberRepository.findByEmail(newMember.getEmail());
        //then
        assertEquals("test@test.com", member.getEmail());
        assertEquals("programmer", member.getJobField());
        assertEquals("testNicks", member.getNickname());
        assertEquals("female", member.getGender());
    }

    @Test
    @DisplayName("signin")
    void successLoginMember() {
        //given
        Member newMember = Member.builder()
                .id(1L)
                .email("test@test.com")
                .jobField("programmer")
                .gender("female")
                .nickname("testNicks")
                .password(passwordEncoder.encode("test123"))
                .build();

        memberRepository.save(newMember);

        SigninRequest request = SigninRequest.builder()
                .email("test@test.com")
                .password("test123")
                .build();

        given(memberRepository.findByEmail(newMember.getEmail()))
                .willReturn(newMember);
        given(memberRepository.existsByEmail(newMember.getEmail())).willReturn(true);
        given(passwordEncoder.matches(request.getPassword(), newMember.getPassword())).willReturn(true);

        //when
        Member member = memberService.signin(request);

        //then
        assertEquals(member.getNickname(), newMember.getNickname());
        assertEquals(member.getEmail(), newMember.getEmail());
        assertEquals(member.getGender(), newMember.getGender());
        assertEquals(member.getJobField(), newMember.getJobField());
    }
}