package com.be.inssagram.domain.stroy.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.follow.dto.response.FollowingList;
import com.be.inssagram.domain.follow.entity.Follow;
import com.be.inssagram.domain.follow.repository.FollowRepository;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.stroy.dto.request.CreateStoryRequest;
import com.be.inssagram.domain.stroy.dto.response.StoryInfoResponse;
import com.be.inssagram.domain.stroy.entity.Story;
import com.be.inssagram.domain.stroy.repository.StoryRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class StoryService {
    private final StoryRepository storyRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    @Transactional
    public StoryInfoResponse createStory(String token, CreateStoryRequest request) {
        Member member = tokenProvider.getMemberFromToken(token);
        Story parentStory = new Story();
        if (!storyRepository.existsByMember(member)) {
            parentStory = Story.builder()
                    .member(member)
                    .parentFlag(true)
                    .childStory(new LinkedList<>())
                    .build();
        } else {
            parentStory = storyRepository.findByMemberIdAndParentFlag(
                            member.getId(), true)
                    .orElseThrow(UserDoesNotExistException::new);
        }

        Story story = Story.builder()
                .member(member)
                .images(request.getImage())
                .fileName(request.getFileName())
                .contents(request.getContents())
                .location(request.getLocation())
                .parentStory(parentStory)
                .build();

        parentStory.getChildStory().add(story);
        parentStory.setUpdatedAt(story.getCreatedAt());
        storyRepository.save(parentStory);

        StoryInfoResponse response = StoryInfoResponse.from(storyRepository.save(story));
        calculatePassedTime(story);
        response.setPassedTime(story.getPassedTime());
        return response;
    }

    @Transactional
    public List<FollowingList> searchMemberHaveStory(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(UserDoesNotExistException::new);

        List<Follow> following = followRepository.findAllByRequesterInfo(member);
        List<FollowingList> followingLists = following.stream()
                .map(follow -> new FollowingList(
                        follow.getFollowingInfo().getId(),
                        follow.getFollowingInfo().getNickname(),
                        follow.getFollowingInfo().getImage()))
                .toList();
        Map<FollowingList, java.time.LocalDateTime> map = new HashMap<>();
        for (FollowingList follower : followingLists) {
            if (storyRepository.existsByMemberIdAndParentFlag(follower.getFollowing_Id(), true)) {
                Story story = storyRepository.findByMemberIdAndParentFlag(follower.getFollowing_Id(), true)
                        .orElseThrow();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                map.put(follower, java.time.LocalDateTime.parse(story.getUpdatedAt(), formatter));
            }
        }
        List<Map.Entry<FollowingList, LocalDateTime>> list = new ArrayList<>(map.entrySet());
        // LocalDate를 기반으로 정렬하는 Comparator 생성
        Comparator<Map.Entry<FollowingList, LocalDateTime>> comparator = Map.Entry.comparingByValue();
        // 내림차순으로 정렬
        list.sort(comparator.reversed());
        return list.stream().map(Map.Entry::getKey).toList();
    }

    @Transactional
    public StoryInfoResponse searchParentStoryByMemberId(Long memberId) {
        Story story = storyRepository.findByMemberIdAndParentFlag(memberId, true)
                .orElseThrow();
        calculatePassedTime(story);
        return StoryInfoResponse.builder()
                .storyId(story.getId())
                .memberId(story.getMember().getId())
                .memberProfile(story.getMember().getImage())
                .memberNickname(story.getMember().getNickname())
                .createdAt(story.getCreatedAt())
                .updatedAt(story.getUpdatedAt())
                .passedTime(story.getPassedTime())
                .build();
    }

    @Transactional
    public List<StoryInfoResponse> searchStoryWithChildStoryByMemberId(Long memberId) {
        Story story = storyRepository.findByMemberIdAndParentFlag(memberId, true)
                .orElseThrow();
        for (Story child : story.getChildStory()) {
            calculatePassedTime(child);
        }
        storyRepository.save(story);
        return story.getChildStory().stream()
                .map(StoryInfoResponse::from).toList();
    }

    @Transactional
    public void deleteStory(Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow();
        if (story.getParentStory() == null) {
            storyRepository.delete(story);
        } else {
            Story parentStory = story.getParentStory();
            if (parentStory.getChildStory().size() == 1) {
                storyRepository.save(parentStory);
                storyRepository.delete(parentStory);
            } else {
                storyRepository.save(story);
                storyRepository.delete(story);
            }
        }
    }

    private void calculatePassedTime(Story story) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime targetDateTime = LocalDateTime.parse(story.getCreatedAt(), formatter);
        Duration duration = Duration.between(LocalDateTime.now(), targetDateTime);
        story.setPassedTime("약 " + String.valueOf(duration.toHours() * -1) + "시간 전");
        storyRepository.save(story);
    }

}
