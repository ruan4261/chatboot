package org.ruan.cb.web;

import com.alibaba.fastjson.JSONObject;
import org.ruan.cb.mod.Message;
import org.ruan.cb.service.ChatService;
import org.ruan.cb.util.RedisZsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;

@ServerEndpoint(value = "/ws")
@Component
@Scope("prototype")
public class WebSocketAPI {

    private static ApplicationContext applicationContext;

    //会话历史id数量
    private static int sidCount = 0;

    private ChatService chatService;

    private RedisZsetUtil redisZsetUtil;

    //本次会话id
    private Integer sid;

    //会话id
    private Session session;

    /**
     * 开启连接
     *
     * @param session
     */
    @OnOpen
    public void open(Session session) {
        chatService = applicationContext.getBean(ChatService.class);
        redisZsetUtil = applicationContext.getBean(RedisZsetUtil.class);
        this.session = session;
        chatService.addListener(session);
        chatService.sendMessage(session, "{\"useid\":" + this.getSid() + "}");
        chatService.sendMessage(session, "连接成功，当前频道有" + chatService.getSessionCount() + "人在线。");
        chatService.sendMessage(session, "您每次发言可使用不同昵称，ID将会被展示给所有人，您的信息身份将会被保存在服务器中，请勿发表违法言论。\n额外注意:本聊天室已开启标签注入！您可以发送图片、视频、超链接及iframe等标签，但不可以发送脚本（已被过滤，请放心使用本聊天室，但请谨慎点击其他用户的超链接）。举报违规信息请联系QQ2598055586。");
        chatService.sendSessionCount();
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void close() {
        try {
            this.session.close();
            chatService.removeSession(this.session);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatService.sendSessionCount();
    }

    /**
     * 收到客户端消息
     *
     * @param message
     */
    @OnMessage
    public void message(String message) {
        try {
            JSONObject jo = JSONObject.parseObject(message);
            if (jo.getString("request").equals("historyLoad")) {
                //请求加载历史信息
                chatService.historyLoad(this.session, jo.getIntValue("hour"));
            } else if (jo.getString("request").equals("sendMessage")) {
                //请求发送信息
                Message mes = new Message(System.currentTimeMillis(), this.getSid(), jo.getString("name").replace("\"","'"), jo.getString("content").replace("\n", "<br>").replace("  ", "&nbsp;&nbsp;").replace("\"","'"), new Date());
                chatService.sendMessageToMQ(mes);
                //加缓存
                redisZsetUtil.add(mes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 取得本会话id
     *
     * @return
     */
    private Integer getSid() {
        if (this.sid != null) return this.sid;
        this.sid = ccSid();
        return this.sid;
    }

    /**
     * 创建本会话id
     *
     * @return
     */
    private synchronized int ccSid() {
        return ++sidCount;
    }

    /**
     * 上下文
     * 辅助ws类注入
     *
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketAPI.applicationContext = applicationContext;
    }
}
