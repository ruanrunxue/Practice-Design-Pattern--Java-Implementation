package com.yrunz.designpattern.mq;

// 消息队列抽象接口
public interface Mq {
    void createTopic(String topic);

    void deleteTopic(String topic);

    void clear();

    void produce(Message message);

    Message consume(String topic);
}
