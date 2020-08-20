package org.ruan.cb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ruan.cb.mod.Message;
import org.ruan.cb.service.ChatService;
import org.ruan.cb.util.RedisZsetUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 队列业务
 */
@Service("chatService")
public class ChatServiceImpl implements ChatService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisZsetUtil redisZsetUtil;

    private Set<Session> sessions = new HashSet<>();

    private static String mesExchange;

    private static String mesQ;


    public void removeSession(Session session) {
        this.sessions.remove(session);
    }

    /**
     * 获取会话数量
     *
     * @return
     */
    @Override
    public int getSessionCount() {
        return sessions.size();
    }

    /**
     * 向交换机发送信息
     *
     * @param message
     */
    @Override
    public void sendMessageToMQ(Message message) {
        amqpTemplate.convertAndSend(mesExchange, "", message.toJsonSerialization());
    }

    /**
     * 增加监听者
     *
     * @param session
     */
    @Override
    public void addListener(Session session) {
        this.sessions.add(session);
    }

    /**
     * 服务器主动发信
     *
     * @param session
     * @param message
     * @throws IOException
     */
    @Override
    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
            sessions.remove(session);
        }
    }

    /**
     * 服务器主动发信
     *
     * @param session
     * @param message
     * @throws IOException
     */
    @Override
    public void sendMessage(Session session, Set<Message> message) {
        JSONArray ja = new JSONArray();
        ja.addAll(message);
        JSONObject jo = new JSONObject();
        jo.put("infoType", "set");
        jo.put("infoSet", ja);
        sendMessage(session, jo.toJSONString());
    }

    /**
     * 向前端推送会话数量（更新）
     */
    @Override
    public void sendSessionCount() {
        JSONObject jo = new JSONObject();
        jo.put("sessionCount", sessions.size());
        this.sessions.forEach(session -> {
            sendMessage(session, jo.toJSONString());
        });
    }

    /**
     * 加载历史信息
     *
     * @param session
     */
    @Override
    public void historyLoad(Session session, int hour) {
        Set<Message> set = redisZsetUtil.getHistoryWithHour(hour);
        if (set == null || set.size() == 0) return;
        sendMessage(session, set);
    }

    /**
     * 监听队列
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "${mq.message.exchange}", type = "fanout"),
            value = @Queue(value = "${mq.message.queue}", durable = "true")
    ))
    @Override
    public void listener(String message) {
        this.sessions.forEach(session -> {
            sendMessage(session, message);
        });
    }

    @Value("${mq.message.exchange}")
    public void setMesExchange(String mesExchange) {
        ChatServiceImpl.mesExchange = mesExchange;
    }

    @Value("${mq.message.queue}")
    public void setMesQ(String mesQ) {
        ChatServiceImpl.mesQ = mesQ;
    }
}
