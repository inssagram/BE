package com.be.inssagram.domain.chat.chatMessage.controller;

import com.be.inssagram.common.ApiResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.request.ChatMessageRequest;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithPostResponse;
import com.be.inssagram.domain.chat.chatMessage.dto.response.ChatMessageWithStoryResponse;
import com.be.inssagram.domain.chat.chatMessage.entity.ChatMessage;
import com.be.inssagram.domain.chat.chatMessage.service.ChatService;
import com.be.inssagram.domain.member.entity.Member;
import com.be.inssagram.domain.notification.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;
    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @GetMapping("/chat/enter/room")
    public ApiResponse<List<ChatMessageResponse>> enter(
            @RequestParam(value = "room-id") Long chatRoomId
            , @RequestHeader("Authorization") String token) {
        return ApiResponse.createSuccess(chatService.enter(token, chatRoomId));
    }

    @GetMapping("/chat")
    public ApiResponse<List<NotificationResponse>> getChatroom(
            @RequestHeader("Authorization") String token) {
        List<NotificationResponse> result = chatService.getChatroomLists(token);
        return ApiResponse.createSuccessWithMessage(result ,"채팅방 목록을 불러왔습니다");
    }

    @GetMapping("/chat/enter-after-search/room/receiver")
    public ApiResponse<List<ChatMessageResponse>> enterAfterSearch(
            @RequestParam(value = "receiverMemberId") Long receiverMemberId
            , @RequestHeader("Authorization") String token) {
        return ApiResponse.createSuccess(chatService.enterAfterSearch(
                token, receiverMemberId));
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public ApiResponse<ChatMessageResponse> send(
            ChatMessageRequest chat, @DestinationVariable Long chatRoomId
            , @Header("token") String token
    ) {
        return ApiResponse.createSuccess(chatService.send(
                token, chat, chatRoomId));
    }

    @PostMapping("/chat/send-in-story")
    public ApiResponse<ChatMessageResponse> sendInStory(
            @RequestBody ChatMessageRequest chat
            , @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.createSuccess(chatService.sendInStory(
                token, chat));
    }

    @PostMapping("/chat/share/story")
    public ApiResponse<ChatMessageResponse> sendWithStory2(
            @RequestBody ChatMessageRequest chat
            , @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.createSuccess(chatService.sendWithStory2(
                token, chat));
    }

    @PostMapping("/chat/share/post")
    public ApiResponse<ChatMessageResponse> sendWithPost2(
            @RequestBody ChatMessageRequest chat
            , @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.createSuccess(chatService.sendWithPost2(
                token, chat));
    }

    @DeleteMapping("/chat/delete/{chatMessageId}")
    private ApiResponse<?> deleteChatMessage(@PathVariable Long chatMessageId) {
        chatService.deleteChatMessage(chatMessageId);
        return ApiResponse.createMessage("삭제");
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatMessage chat) {
        System.out.println("received : " + chat.getMessage());
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

}