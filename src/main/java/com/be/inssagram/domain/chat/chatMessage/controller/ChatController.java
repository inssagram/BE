package com.be.inssagram.domain.chat.chatMessage.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.request.ChatMessageRequest;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageOnlyResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithPostResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithStoryResponse;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.chat.chatMessage.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    //    @MessageMapping("chat.enter.{chatRoomId}")
    @GetMapping("/chat/enter/room")
    public ApiResponse<List<List<?>>> enter(
            @RequestParam(value = "room-id") Long chatRoomId
            , @RequestHeader("Authorization") String token) {
        return ApiResponse.createSuccess(chatService.enter(token, chatRoomId));
    }

    @GetMapping("/chat/enter-after-search/room")
    public ApiResponse<List<List<?>>> enterAfterSearch(
            @RequestBody ChatMessageRequest request
            , @RequestHeader("Authorization") String token) {
        return ApiResponse.createSuccess(chatService.enterAfterSearch(
                token, request));
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public ApiResponse<ChatMessageOnlyResponse> send(
            ChatMessageRequest chat, @DestinationVariable Long chatRoomId
            , @Header("token") String token
    ) {
        return ApiResponse.createSuccess(chatService.send(
                token, chat, chatRoomId));
    }

    @MessageMapping("chat.messageWithStory.{chatRoomId}")
    public ApiResponse<ChatMessageWithStoryResponse> sendWithStory(
            ChatMessageRequest chat, @Header("token") String token
            , @DestinationVariable Long chatRoomId
    ) {
        return ApiResponse.createSuccess(chatService.sendWithStory(
                token, chat, chatRoomId));
    }

    @MessageMapping("chat.messageWithPost.{chatRoomId}")
    public ApiResponse<ChatMessageWithPostResponse> sendWithPost(
            ChatMessageRequest chat, @Header("token") String token
            , @DestinationVariable Long chatRoomId
    ) {
        return ApiResponse.createSuccess(chatService.sendWithPost(
                token, chat, chatRoomId));
    }

    @PostMapping("/share/story")
    public void sendWithStory2(
            @RequestBody ChatMessageRequest chat
            , @RequestHeader("Authorization") String token
    ) {
        chatService.sendWithStory2(
                token, chat);
    }

    @PostMapping("/share/post")
    public void sendWithPost2(
            @RequestBody ChatMessageRequest chat
            , @RequestHeader("Authorization") String token
    ) {
        chatService.sendWithPost2(
                token, chat);
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatMessage chat) {
        System.out.println("received : " + chat.getMessage());
    }

}