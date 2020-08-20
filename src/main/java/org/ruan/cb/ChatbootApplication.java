package org.ruan.cb;

import org.ruan.cb.web.WebSocketAPI;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableRabbit
@EnableWebSocket
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ChatbootApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ChatbootApplication.class, args);
        WebSocketAPI.setApplicationContext(applicationContext);
    }

}
