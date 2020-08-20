package org.ruan.cb.service;

import org.ruan.cb.mod.Message;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Set;

public interface ChatService {
    public void removeSession(Session session);

    public int getSessionCount();

    public void listener(String message);

    public void addListener(Session session);

    public void sendMessageToMQ(Message message);

    public void sendMessage(Session session, String message);

    public void sendMessage(Session session, Set<Message> message);

    public void sendSessionCount();

    public void historyLoad(Session session, int hour);
}
