package org.ruan.cb.util;

import org.ruan.cb.mod.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * zset工具
 */
@Component("redisZsetUtil")
public class RedisZsetUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public boolean add(Message message) {
        if (message == null) return false;
        try {
            return redisTemplate.opsForZSet().add("message", message.toJsonSerialization(), message.getScore());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Set<Message> getHistoryWithHour(int hour) {
        long s = System.currentTimeMillis();
        try {
            return (Set<Message>) (Object) redisTemplate.opsForZSet().rangeByScore("message", (s - (3600000 * hour)), s);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
