package org.ruan.cb.mod;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class Message {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    //用于缓存键的值
    private long score;
    private Integer sid;
    private String name;
    private String content;
    private Date pubTime;

    public String toJsonSerialization() {
        return "{\"score\":\"" + score + "\",\"sid\":" + "\"" + sid + "\",\"name\":" + "\"" + name + "\",\"content\":" + "\"" + content + "\",\"pubTime\":\"" + dateFormat.format(pubTime) + "\"}";
    }

    public Message(String jsonSerialization) {
        JSONObject jsonObject = JSONObject.parseObject(jsonSerialization);
        this.score = jsonObject.getLong("score");
        this.sid = jsonObject.getInteger("sid");
        this.name = jsonObject.getString("name");
        this.content = jsonObject.getString("content");
        this.pubTime = jsonObject.getDate("pubTime");
    }
}
