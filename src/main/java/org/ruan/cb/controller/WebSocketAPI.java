package org.ruan.cb.controller;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint("")
public class WebSocketAPI {

    //会话id
    private Session session;

    /**
     * 开启连接
     *
     * @param session
     */
    @OnOpen
    public void open(Session session) {

    }

    /**
     * 关闭连接
     *
     * @param session
     */
    @OnClose
    public void close(Session session) {

    }

    /**
     * 收到客户端消息
     *
     * @param session
     * @param message
     */
    @OnMessage
    public void message(Session session, String message) {


    }

    /**
     * 服务器主动发信
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

}
