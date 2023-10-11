package com.be.inssagram.domain.member.service;


import com.be.inssagram.domain.member.documents.MemberSearchRepository;
import com.be.inssagram.domain.member.documents.SearchMember;
import com.be.inssagram.domain.member.dto.request.*;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Auth;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.AuthRepository;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberSearchRepository memberSearchRepository;

    //회원가입
    public void signup (SignupRequest request) {
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if(exists){
            throw new DuplicatedUserException();
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(setAccount(request));
    }

    //인증코드 확인 및 삭제
    public void authCodeCheck (AuthenticationRequest request) {
        Auth tempData = authRepository.findByEmailAndCode(request.getEmail(), request.getCode())
                .orElseThrow(WrongAuthInfoException::new);
        authRepository.delete(tempData);
    }

    //사용할수 있는 이메일인지 확인
    public void checkEmailAvailability(AuthenticationRequest request){
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if(exists){
            throw new DuplicatedUserException();
        }
    }

    //사용할수 있는 닉네임인지 확인
    public void checkNicknameAvailability(AuthenticationRequest request){
        boolean exists = memberRepository.existsByNickname(request.getNickName());
        if(exists){
            throw new DuplicatedUserException();
        }
    }

    //MySQL DB에 있는 모든 정보를 ES 에 저장
    @Transactional
    public void saveAllMemberDocuments(){
        List<SearchMember> memberDocumentList = memberRepository.findAll().stream()
                .map(SearchMember::from).collect(Collectors.toList());
        memberSearchRepository.saveAll(memberDocumentList);
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

    //회원 상세조회
    public InfoResponse getMemberDetail(String nickname){
        Member member = memberRepository.findByNickname(nickname);
        return InfoResponse.fromEntity(member);
    }

    private Member setAccount (SignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role("ROLE_MEMBER")
                .nickname(request.getNickname())
                .companyName(request.getCompanyName())
                .build();
    }

}
