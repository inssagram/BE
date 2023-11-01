package com.be.inssagram.domain.member.service;


import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.elastic.documents.repository.MemberSearchRepository;
import com.be.inssagram.domain.elastic.documents.index.MemberIndex;
import com.be.inssagram.domain.follow.dto.response.FollowerList;
import com.be.inssagram.domain.follow.dto.response.FollowingList;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.dto.request.*;
import com.be.inssagram.domain.member.dto.response.DetailedInfoResponse;
import com.be.inssagram.domain.member.dto.response.InfoResponse;
import com.be.inssagram.domain.member.entity.Auth;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.AuthRepository;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.exception.member.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberSearchRepository memberSearchRepository;
    private final FollowRepository followRepository;
    private final TokenProvider tokenProvider;
    private final MailService mailService;

    //회원가입
    public void signup (SignupRequest request) {
        boolean exists = memberRepository.existsByEmail(request.getEmail());
        if(exists){
            throw new DuplicatedUserException();
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Member member = memberRepository.save(setAccount(request));
        //Elastic Search 에 반영
        memberSearchRepository.save(MemberIndex.from(member));
    }

    //인증코드 발급
    public void sendCode (AuthenticationRequest request) {
        Instant currentTime = Instant.now();
        Auth existingAuth = authRepository.findByEmail(request.getEmail()).orElse(null);

        if (existingAuth == null || Duration.between(existingAuth.getCreatedAt(),
            currentTime).toMillis() >= 2 * 60 * 1000) {
            int authCode = mailService.sendMail(request);
            String code = String.valueOf(authCode);
            if (existingAuth == null) {
                existingAuth = new Auth();
                existingAuth.setEmail(request.getEmail());
            }
            existingAuth.setCode(code);
            existingAuth.setCreatedAt(currentTime);
            authRepository.save(existingAuth);
        } else {
            throw new AuthCodeAlreadySentException();
        }
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
    public InfoResponse updateMember(Long id, UpdateRequest request, String token) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
        String requestEmail = tokenProvider.getEmailFromToken(token);
        if(member.getEmail().equals(requestEmail) == false){
            throw new UnauthorizedRequestException();
        }
        if(request.getPassword() != null) {
            if(!passwordEncoder.matches(request.getPassword(), member.getPassword())){
                throw new SamePasswordException();
            }
            request.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        member.updateFields(request);
        Member updatedMember = memberRepository.save(member);
        //ES에 수정된 정보 반영
        memberSearchRepository.save(MemberIndex.from(updatedMember));
        return InfoResponse.fromEntity(member);
    }

    //회원탈퇴
    public void deleteMember(Long id, String token){
        Member member = memberRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException());
        String requestEmail = tokenProvider.getEmailFromToken(token);
        if(member.getEmail().equals(requestEmail) == false){
            throw new UnauthorizedRequestException();
        }
        memberRepository.delete(member);
        memberSearchRepository.delete(MemberIndex.from(member));
    }

    //회원 상세조회
    public DetailedInfoResponse getMemberDetail(String nickname){
        Member member = memberRepository.findByNickname(nickname);
        List<Follow> followers = followRepository.findAllByMemberId(member.getId());
        List<Follow> following = followRepository.findAllByMyId(member.getId());

        List<FollowingList> followingLists = following.stream()
                .map(follow -> new FollowingList(follow.getMemberId(), follow.getMemberName()))
                .collect(Collectors.toList());

        List<FollowerList> followerLists = followers.stream()
                .map(follow -> new FollowerList(follow.getMyId(), follow.getMyName()))
                .collect(Collectors.toList());

        return DetailedInfoResponse.fromEntity(member, followingLists, followerLists);
    }

    private Member setAccount (SignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role("ROLE_MEMBER")
                .nickname(request.getNickname())
                .job(request.getJob())
                .build();
    }
}
