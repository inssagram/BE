package com.be.inssagram.domain.chat.chatMessage.service;

import com.be.inssagram.domain.chat.chatMessage.dto.request.ChatMessageRequest;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageOnlyResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithPostResponse;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.chat.chatMessage.repository.ChatMessageRepository;
import com.be.inssagram.domain.post.entity.Post;
import com.be.inssagram.domain.post.repository.PostRepository;
import com.be.inssagram.exception.post.PostDoesNotExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

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

    public ChatMessageOnlyResponse enter(ChatMessageRequest request, Long chatRoomId) {

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(request.getChatRoomId())
                .sender("[알림]")
                .message(request.getSender() + "님이(가) 입장하셨습니다.")
                .build();

        ChatMessageOnlyResponse response = ChatMessageOnlyResponse.from(
                chatMessageRepository.save(chatMessage));

        template.convertAndSend(
                CHAT_EXCHANGE_NAME, "room." + chatRoomId, response); // exchange

        return response;
    }

    public ChatMessageOnlyResponse send(ChatMessageRequest request, Long chatRoomId) {

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(request.getChatRoomId())
                .sender(request.getSender())
                .message(request.getMessage())
                .build();

        ChatMessageOnlyResponse response = ChatMessageOnlyResponse.from(
                chatMessageRepository.save(chatMessage));

        template.convertAndSend(
                CHAT_EXCHANGE_NAME, "room." + chatRoomId, response); // exchange

        return response;
    }

    public ChatMessageWithPostResponse sendWithPost(ChatMessageRequest request, Long chatRoomId) {

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(request.getChatRoomId())
                .sender(request.getSender())
                .message(request.getMessage())
                .build();
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



//    // 채팅 내용을 파일로 부터 읽어온다.
//    private String readFile() {
//
//        // d드라이브의 chat 폴더의 chat 파일
//        File file = new File("d:\\chat\\chat");
//        // 파일 있는지 검사
//        if (!file.exists()) {
//            return "";
//        }
//        // 파일을 읽어온다.
//        try (FileInputStream stream = new FileInputStream(file)) {
//            return new String(stream.readAllBytes());
//        } catch (Throwable e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//    // 파일를 저장하는 함수
//    private void saveFile(String sender, String message) {
//
//        String path = System.getProperty("user.dir")
//                + File.separator	// Windows('\'), Linux, MAC('/')
//                + "inssagram"
//                + File.separator
//                + ""
//                ;
//
//        // 메시지 내용
//        String msg = sender + "]  " + message + "\n";
//        // 파일을 저장한다.
//        try (FileOutputStream stream = new FileOutputStream("d:\\chat\\chat", true)) {
//            stream.write(msg.getBytes("UTF-8"));
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }


}
