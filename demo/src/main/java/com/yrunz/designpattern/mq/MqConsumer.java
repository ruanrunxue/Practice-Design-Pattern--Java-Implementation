package com.yrunz.designpattern.mq;

// 消费者接口，从消息队列中消费数据
public interface MqConsumer {
    Message consume(String topic);
}
