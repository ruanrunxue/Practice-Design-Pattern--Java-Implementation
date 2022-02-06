package com.yrunz.designpattern.mq;

public class TopicAlreadyExistException extends RuntimeException {
    public TopicAlreadyExistException(String topic) {
        super("topic " + topic + " already exist");
    }
}
