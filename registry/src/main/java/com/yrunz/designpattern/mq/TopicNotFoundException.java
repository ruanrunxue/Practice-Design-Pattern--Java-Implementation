package com.yrunz.designpattern.mq;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String topic) {
        super("topic " + topic + " not found");
    }
}
