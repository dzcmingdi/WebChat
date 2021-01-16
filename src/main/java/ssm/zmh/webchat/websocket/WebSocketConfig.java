//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.websocket;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
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
import ssm.zmh.webchat.model.user.Authentication;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/subscribe/user/");
        config.setUserDestinationPrefix("/subscribe/user/");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/webchat").setAllowedOrigins("*").withSockJS();
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String userUUID = (Objects.requireNonNull(accessor.getNativeHeader("userUUID"))).get(0);
                    accessor.setUser(new Authentication(userUUID));
                    WebSocketModel.userSocketSessionsSet.add(userUUID);
                    return message;
                } else {
                    if (accessor != null && StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                        Principal principal = accessor.getUser();
                        if (principal != null) {
                            WebSocketModel.userSocketSessionsSet.remove((accessor.getUser()).getName());
                        }
                    }

                    return message;
                }
            }
        });
    }
}
