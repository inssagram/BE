package com.be.inssagram.domain.stroy;

import com.be.inssagram.domain.stroy.entity.Story;
import com.be.inssagram.domain.stroy.repository.StoryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StoryScheduler {

    private final StoryRepository storyRepository;

    public StoryScheduler(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    // 매 시 정각에 실행
    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldStories() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime twentyFourHoursAgo = currentDateTime.minusSeconds(30);
        String formattedTwentyFourHoursAgo = twentyFourHoursAgo.format(formatter);

        List<Story> storiesToDelete = storyRepository.findStoriesCreatedBefore(formattedTwentyFourHoursAgo);

        // Story를 삭제하는 로직을 추가
        storyRepository.deleteAll(storiesToDelete);
    }
}
