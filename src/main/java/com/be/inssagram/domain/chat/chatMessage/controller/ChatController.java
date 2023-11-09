package com.be.inssagram.domain.chat.chatMessage.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.request.ChatMessageRequest;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageOnlyResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithPostResponse;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.chat.chatMessage.service.ChatService;
import com.be.inssagram.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    private final PostRepository postRepository;

    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("chat.enter.{chatRoomId}")
    public ApiResponse<ChatMessageOnlyResponse> enter(
            ChatMessageRequest chat, @DestinationVariable Long chatRoomId) {
        return ApiResponse.createSuccess(chatService.enter(chat, chatRoomId));
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public ApiResponse<ChatMessageOnlyResponse> send(
            ChatMessageRequest chat, @DestinationVariable Long chatRoomId) {
        return ApiResponse.createSuccess(chatService.send(chat,chatRoomId));
    }

    @MessageMapping("chat.messageWithPost.{chatRoomId}")
    public ApiResponse<ChatMessageWithPostResponse> sendWithPost(
            ChatMessageRequest chat, @DestinationVariable Long chatRoomId) {
        return ApiResponse.createSuccess(chatService.sendWithPost(chat,chatRoomId));
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatMessage chat) {
        System.out.println("received : " + chat.getMessage());
    }

}