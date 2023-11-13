package com.be.inssagram.domain.chat.chatMessage.service;

import com.be.inssagram.config.Jwt.TokenProvider;
import com.be.inssagram.domain.chat.ChatMessageType;
import com.be.inssagram.domain.chat.chatMessage.dto.request.ChatMessageRequest;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageOnlyResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithPostResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithStoryResponse;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.chat.chatMessage.repository.ChatMessageRepository;
import com.be.inssagram.domain.chat.chatRoom.dto.request.ChatRoomRequest;
import com.be.inssagram.domain.chat.chatRoom.repository.ChatRoomRepository;
import com.be.inssagram.domain.chat.chatRoom.service.ChatRoomService;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.member.repository.MemberRepository;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.domain.stroy.entity.Story;
import com.be.inssagram.domain.stroy.repository.StoryRepository;
import com.be.inssagram.exception.member.UserDoesNotExistException;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

    private final RabbitTemplate template;

    private final PostRepository postRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;

    private final ChatRoomService chatRoomService;

    private TokenProvider tokenProvider;

    // 검색해서 나온 사람한테 메일 보낼 때. 이걸 사용하면 될 듯
    @Transactional
    public ChatMessageOnlyResponse enter(String token, ChatMessageRequest request, Long chatRoomId) {

        Member member = tokenProvider.getMemberFromToken(token);

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(request.getChatRoomId())
                .sender(member)
                .message(member.getNickname()+ "님이(가) 입장하셨습니다.")
                .build();

        ChatMessageOnlyResponse response = ChatMessageOnlyResponse.from(
                chatMessageRepository.save(chatMessage));

        template.convertAndSend(
                CHAT_EXCHANGE_NAME, "room." + chatRoomId, response); // exchange

        return response;
    }

    // 방안에서 메시지 보낼 때.
    @Transactional
    public ChatMessageOnlyResponse send(String token, ChatMessageRequest request, Long chatRoomId) {

        Member sender = tokenProvider.getMemberFromToken(token);
        Member receiver = memberRepository.findById(request.getReceiverMemberId())
                .orElseThrow(UserDoesNotExistException::new);

        ChatMessage chatMessage = getChatMessage(request, sender, receiver);

        ChatMessageOnlyResponse response = ChatMessageOnlyResponse.from(
                chatMessageRepository.save(chatMessage));

        template.convertAndSend(
                CHAT_EXCHANGE_NAME, "room." + chatRoomId, response); // exchange

        return response;
    }

    // post와 스토리를 보낼 때 이렇게.
    @Transactional
    public ChatMessageWithStoryResponse sendWithStory(String token, ChatMessageRequest request) {

        Member sender = tokenProvider.getMemberFromToken(token);
        Member receiver = memberRepository.findById(request.getReceiverMemberId())
                .orElseThrow(UserDoesNotExistException::new);

        Long chatRoomId = getChatRoomId(request, sender);

        ChatMessage chatMessage = getChatMessage(request, sender, receiver);

        if (request.getStoryId() != null) {
            Story story = storyRepository.findById(request.getStoryId()).orElseThrow();
            chatMessage.setStory(story);
        }

        ChatMessageWithStoryResponse response = ChatMessageWithStoryResponse.from(
                chatMessageRepository.save(chatMessage));

        template.convertAndSend(
                CHAT_EXCHANGE_NAME, "room." + chatRoomId, response); // exchange

        return response;
    }

    @Transactional
    public ChatMessageWithPostResponse sendWithPost(String token, ChatMessageRequest request) {

        Member sender = tokenProvider.getMemberFromToken(token);
        Member receiver = memberRepository.findById(request.getReceiverMemberId())
                .orElseThrow(UserDoesNotExistException::new);

        Long chatRoomId = getChatRoomId(request, sender);

        ChatMessage chatMessage = getChatMessage(request, sender, receiver);

        if (request.getPostId() != null) {
            Post post = postRepository.findById(request.getPostId())
                    .orElseThrow(PostDoesNotExistException::new);
            chatMessage.setPost(post);
        }

        ChatMessageWithPostResponse response = ChatMessageWithPostResponse.from(
                chatMessageRepository.save(chatMessage));

        template.convertAndSend(
                CHAT_EXCHANGE_NAME, "room." + chatRoomId, response);

        return response;

    }

    // 내부 메서드
    private Long getChatRoomId(ChatMessageRequest request, Member member) {
        Long firstId;
        Long secondId;
        Long chatRoomId;
        if (member.getId() > request.getReceiverMemberId()) {
            firstId = request.getReceiverMemberId();
            secondId = member.getId();
        } else {
            firstId = member.getId();
            secondId = request.getReceiverMemberId();
        }

        if (chatRoomRepository.findByFirstParticipantIdAndSecondParticipantId(
                firstId, secondId).getRoomId() != null) {
            chatRoomId = chatRoomRepository
                    .findByFirstParticipantIdAndSecondParticipantId(firstId, secondId)
                    .getRoomId();
        } else {
            chatRoomId = chatRoomService.createChatRoom(ChatRoomRequest.builder()
                    .firstParticipantId(firstId)
                    .secondParticipantId(secondId)
                    .build()).getChatRoomId();
        }

        return chatRoomId;
    }

    private static ChatMessage getChatMessage(
            ChatMessageRequest request, Member sender, Member receiver) {
        String image = "";
        if (request.getImageUrl() != null) {
            image = request.getImageUrl();
        }
        return ChatMessage.builder()
                .roomId(request.getChatRoomId())
                .sender(sender)
                .receiver(receiver)
                .type(ChatMessageType.message)
                .message(request.getMessage())
                .image(image)
                .build();
    }


    // 채팅 내용을 파일로 부터 읽어온다.
    private String readFile() {

        // d드라이브의 chat 폴더의 chat 파일
        File file = new File("d:\\chat\\chat");
        // 파일 있는지 검사
        if (!file.exists()) {
            return "";
        }
        // 파일을 읽어온다.
        try (FileInputStream stream = new FileInputStream(file)) {
            return new String(stream.readAllBytes());
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }
    // 파일를 저장하는 함수
    private void saveFile(String sender, String message) {

        String path = System.getProperty("user.dir")
                + File.separator	// Windows('\'), Linux, MAC('/')
                + "inssagram"
                + File.separator
                + ""
                ;

        // 메시지 내용
        String msg = sender + "]  " + message + "\n";
        // 파일을 저장한다.
        try (FileOutputStream stream = new FileOutputStream("d:\\chat\\chat", true)) {
            stream.write(msg.getBytes("UTF-8"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
