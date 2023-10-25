package com.be.inssagram.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.*;

//Stomp MessageBroker 설정
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 각 유저에 대한 세션키(socket id)를 저장할 용도의 map
     */
    private static final Map<String, String> sessionKeys = new HashMap<>();
    /**
     * RabbitMQ Datasource
     */
    private final RabbitTemplate rabbitTemplate;


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 메시지채널에서 주고받는 메시지를 확인 및/또는 수정할 수 있는 인터셉터 추가
        registration.interceptors(new ChannelInterceptor() {
            // 메시지가 실제로 채널로 전송되기 전에 호출됨
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor headerAccessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // 헤더에서 유저명을 가져온다.
                List<String> usernames = Optional
                        .ofNullable(headerAccessor.getNativeHeader("username"))
                        .orElseGet(Collections::emptyList);

                // 처음 접속 시도시 유저 데이터를 심어준다.
                if (StompCommand.CONNECT.equals(headerAccessor.getCommand()) && !usernames.isEmpty()) {
                    headerAccessor.setUser(new Principal() {
                        @Override
                        public String getName() {
                            return usernames.get(0);
                        }
                    });
                    sessionKeys.put(usernames.get(0), headerAccessor.getSessionId());
                }

                // 사용자 접속 해제시 사용자 큐를 삭제한다.
                if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())) {
                    String sessionKey = sessionKeys.get(headerAccessor.getUser().getName());
                    // message-user는 생성되는 큐의 접두사
                    // /users/queue/message의 message를 가져와 -user를 붙여만든다.
                    new RabbitAdmin(rabbitTemplate).deleteQueue("message-user" + sessionKey);
                    sessionKeys.remove(sessionKey);
                }

                return message;
            }
        });
    }

    /**
     * 클라이언트에서 websocket에 접속하는 endpoint를 등록한다.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://*.*.*.*:8080", "http://*:8080") //안해도 무관
                .setAllowedOrigins("*");
    }

    /**
     * Stomp사용을 위한 Message Broker 설정을 해주는 메소드
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub") // 메세지를 보낼(publish) 경로를 설정
                .setUserDestinationPrefix("/users")  // 특정 사용자에게 메시지 전송시 사용할 주소
                .enableStompBrokerRelay("/queue", "/topic", "/exchange") // 메세지 수신(Subscribe), 경로를 설정해주는 메서드
                .setRelayHost("localhost")
                .setVirtualHost("/")
                .setRelayPort(61613) // RabbitMQ STOMP 기본 포트
                .setSystemLogin("guest")
                .setSystemPasscode("guest")
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }


}
