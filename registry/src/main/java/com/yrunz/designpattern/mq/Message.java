package com.yrunz.designpattern.mq;

public class Message {
    // 消息所属topic
    private final String topic;
    // 消息负载
    private final String payload;

    private Message (String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public static Message of(String topic, String payload) {
        return new Message(topic, payload);
    }

    public String topic() {
        return topic;
    }

    public String payload() {
        return payload;
    }
}
