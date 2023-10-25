package com.be.inssagram.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/stomp")
@RequiredArgsConstructor
public class StompController {

    // 간단한 메시징 프로토콜(예: STOMP)에 사용하기 위한 메서드가 포함된 MessageSendingOperations의 구현체
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * /topic/wiki로 데이터를 publish한다.
     */
    @GetMapping("/topic")
    @ResponseBody
    public String publishTopicMessage() {
        Map<String, String> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", "value2");

        simpMessageSendingOperations.convertAndSend("/topic/wiki", data);

        return "OK";
    }

    /**
     * /pub/send/message주소로 받은 메시지를 처리한다.
     * @param params
     */
    @MessageMapping("/send/message")
    public void pubSendMessage(Map<String, String> params) {
        String targetUsername = params.get("targetUsername");
        String message = params.get("message");
        String sender = params.get("sender");

        // 전송자와 메시지 내용을 데이터로 보낸다.
        Map<String, String> data = new HashMap<>();
        data.put("message", message);
        data.put("sender", sender);

        // convertAndSendToUser로 특정 유저의 큐에 데이터를 넣어준다.
        this.simpMessageSendingOperations.convertAndSendToUser(
                targetUsername, "/queue/message",  data
        );
    }
}
