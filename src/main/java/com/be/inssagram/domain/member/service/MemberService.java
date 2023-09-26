package com.be.inssagram.domain.member.service;


import com.be.inssagram.domain.member.dto.request.AuthenticationRequest;
import com.be.inssagram.domain.member.dto.request.SigninRequest;
import com.be.inssagram.domain.member.dto.request.SignupRequest;
import com.be.inssagram.domain.member.dto.request.UpdateRequest;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Auth;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.AuthRepository;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public void signup (SignupRequest request) {
        Auth tempData = authRepository.findByEmailAndCode(request.getEmail(), request.getAuthNumber())
                .orElseThrow(WrongAuthInfoException::new);
        authRepository.delete(tempData);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(setAccount(request));
    }

    //사용할수 있는 이메일인지 확인
    public void checkAvailability (AuthenticationRequest request){
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if(exists){
            throw new DuplicatedUserException();
        }
    }

    //로그인
    public Member signin(SigninRequest request) {
        boolean checkMember = memberRepository.existsByEmail(request.getEmail());

        if(checkMember == false){
            throw new WrongEmailException();
        }
        Member member = memberRepository.findByEmail(request.getEmail());

        if (!this.passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new WrongPasswordException();
        }

        return member;
    }

    //회원정보 수정
    public InfoResponse updateMember(Long id, UpdateRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
        if(request.getPassword() != null) {
            if(!passwordEncoder.matches(request.getPassword(), member.getPassword())){
                throw new SamePasswordException();
            }
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        member.updateFields(request);
        memberRepository.save(member);
        return InfoResponse.fromEntity(member);
    }

    //회원탈퇴
    public void deleteMember(Long id){
        Member member = memberRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
        memberRepository.delete(member);
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
