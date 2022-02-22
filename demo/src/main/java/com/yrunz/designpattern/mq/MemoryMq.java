package com.yrunz.designpattern.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

// 懒汉式单例模式，双重校验锁
// 内存消息队列，使用Message作为通信载体
// 队列内划分多个topic，使用者必须先创建topic后再进行通信
public class MemoryMq implements MqConsumer, MqProducer {
    private volatile static MemoryMq INSTANCE = null;
    // key为Topic
    private final Map<String, Queue<Message>> queues;

    private MemoryMq() {
        queues = new HashMap<>();
    }

    public static MemoryMq instance() {
        if (INSTANCE == null) {
            synchronized (MemoryMq.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MemoryMq();
                }
            }
        }
        return INSTANCE;
    }

    public void clear() {
        queues.clear();
    }

    @Override
    public void produce(Message message) {
        queues.putIfAbsent(message.topic(), new LinkedBlockingQueue<>());
        queues.get(message.topic()).offer(message);
    }

    @Override
    public Message consume(String topic) {
        queues.putIfAbsent(topic, new LinkedBlockingQueue<>());
        return queues.get(topic).poll();
    }
}
