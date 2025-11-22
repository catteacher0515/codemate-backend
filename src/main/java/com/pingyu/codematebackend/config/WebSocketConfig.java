package com.pingyu.codematebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor; // 【关键武器】

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/codemate")
                .setAllowedOriginPatterns("*") // 允许跨域
                // 【关键战术】添加握手拦截器
                // 这行代码会自动把 HttpSession 中的所有属性（包括 loginUser）
                // 复制到 WebSocket 的 attributes 中，让我们在 Controller 里能拿到用户！
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 1. 广播频道前缀：/topic/team/1
        registry.enableSimpleBroker("/topic");
        // 2. 前端发送前缀：/app/chat/...
        registry.setApplicationDestinationPrefixes("/app");
    }
}