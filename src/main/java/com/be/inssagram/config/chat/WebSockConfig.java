package com.be.inssagram.config.chat;

import com.be.inssagram.domain.chat.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    @Value("${spring.rabbitmq.host}")
    private String dockerUrl;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setPathMatcher(new AntPathMatcher(".")); // URL을 / -> .으로
        config.setApplicationDestinationPrefixes("/pub");
        config.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
                .setRelayHost(dockerUrl)
                .setRelayPort(61613)
                .setVirtualHost("/")
                .setSystemLogin("guest")
                .setSystemPasscode("guest")
                .setClientLogin("guest")
                .setClientPasscode("guest")
        ;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("http://localhost:3000",
                        "https://inssagram-rb9u8ngxq-joshyeom.vercel.app/",
                        "https://inssagram-fe.vercel.app/",
                        "https://fe-lyart-nu.vercel.app/",
                        "https://fe-joshyeom.vercel.app/")
                .withSockJS()
                // .setHeartbeatTime(5000)  // 하트비트 주기: 5초
                // .setDisconnectDelay(5000)  // 연결 종료 지연: 5초
                ;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
