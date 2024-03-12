package com.udabe.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



/*cho phép ứng dụng của chúng ta trở thành một WebSocketMessageBroker*/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        //Đặt địa điểm đường dẫn mà broker gửi lên để đăng ký với SockJS:
        registry.enableSimpleBroker("/topic");

        //Đặt địa điểm đường dẫn cho @MessageMapping để FE nhận biết và trả về:
        registry.setApplicationDestinationPrefixes("/ws");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/api/our-websocket")
                .setHandshakeHandler(new UserHandshakeHandler()).setAllowedOriginPatterns("*")
                .withSockJS();
    }


}

/*
Phương thức configureMessageBroker() được sử dụng để cấu hình MessageBrokerRegistry, cho biết địa chỉ mà các tin nhắn sẽ được gửi đến và nhận từ đâu.
Đoạn mã này thiết lập broker đơn giản sử dụng địa chỉ "/topic". Điều này cho phép các tin nhắn được gửi đến tất cả các khách hàng
đang đăng ký với địa chỉ "/topic". Các tin nhắn được gửi từ khách hàng đến máy chủ sẽ được định tuyến theo đường dẫn "/ws".*/


/*
Phương thức registerStompEndpoints() được sử dụng để đăng ký một điểm cuối của WebSocket, để máy khách có thể kết nối đến. Ở đây, chúng ta đăng ký
một endpoint tên là "/our-websocket", và cũng đặt cấu hình cho SockJS. SockJS là một thư viện JavaScript cho phép sử dụng WebSocket bất kể
trình duyệt của người dùng hỗ trợ hay không. Nó cung cấp một lớp trừu tượng cho các kết nối WebSocket, cho phép ứng dụng của bạn hoạt động trên
hầu hết các trình duyệt hiện đại. Để xử lý yêu cầu handshake từ khách hàng, chúng ta sử dụng UserHandshakeHandler() để xác thực khách hàng và
cấp quyền truy cập cho họ.*/
