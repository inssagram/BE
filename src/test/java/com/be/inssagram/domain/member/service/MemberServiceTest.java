package com.be.inssagram.domain.member.service;

import com.be.inssagram.domain.member.dto.request.SigninRequest;
import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("update")
    void successUpdateAccount(){
        //given
        Member newMember = Member.builder()
                .id(1L)
                .email("test@test.com")
                .jobField("programmer")
                .gender("female")
                .nickname("testNicks")
                .password("test123")
                .build();

        memberRepository.save(newMember);
        given(memberRepository.save(any())).willReturn(newMember);
        given(memberRepository.findById(newMember.getId()))
                .willReturn(Optional.of(newMember));

        //when
        UpdateRequest request = UpdateRequest.builder()
                .nickname("updatedNick")
                .email("changed@test.com")
                .gender("male")
                .jobField("waiter")
                .build();

        memberService.updateMember(newMember.getId(), request);

        Optional<Member> member = memberRepository.findById(1L);
        //then
        assertEquals("updatedNick", member.get().getNickname());
        assertEquals("changed@test.com", member.get().getEmail());
        assertEquals("male", member.get().getGender());
        assertEquals("waiter", member.get().getJobField());
    }

    @Test
    @DisplayName("delete")
    void successDeleteAccount(){
        //given
        Member newMember = Member.builder()
                .id(1L)
                .email("test@test.com")
                .jobField("programmer")
                .gender("female")
                .nickname("testNicks")
                .password("test123")
                .build();

        memberRepository.save(newMember);

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(newMember));
        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        //when
        memberService.deleteMember(1L);
        //then
        verify(memberRepository, times(1)).save(captor.capture());
        assertEquals(1, captor.getValue().getId());
    }
}