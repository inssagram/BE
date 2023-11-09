package com.be.inssagram.domain.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketChatEventListener {
    private static final Logger log= LoggerFactory.getLogger(WebSocketChatEventListener.class);

    private final SimpMessageSendingOperations messagingTemplate;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event){
        log.info("===========");
        log.info("new web socket connection");
        log.info("===========");
    }

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
//        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
//        String username=(String)headerAccessor.getSessionAttributes().get("username");
//        if(username != null) {
//            CustomMessage chatMessage = new CustomMessage("Leave",username,"나감");
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }
}