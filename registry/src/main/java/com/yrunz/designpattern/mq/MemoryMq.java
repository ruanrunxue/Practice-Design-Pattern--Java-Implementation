package com.yrunz.designpattern.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

// 内存消息队列，使用Message作为通信载体
// 队列内划分多个topic，使用者必须先创建topic后再进行通信
public class MemoryMq {
    private static final MemoryMq INSTANCE = new MemoryMq();
    // key为Topic
    private final Map<String, Queue<Message>> queues;

    private MemoryMq() {
        queues = new HashMap<>();
    }

    public static MemoryMq instance() {
        return INSTANCE;
    }

    public void createTopic(String topic) {
        if (queues.containsKey(topic)) {
            throw new TopicAlreadyExistException(topic);
        }
        queues.put(topic, new LinkedBlockingQueue<>());
    }

    public void produce(Message message) {
        if (!queues.containsKey(message.topic())) {
            throw new TopicNotFoundException(message.topic());
        }
        queues.get(message.topic()).offer(message);
    }

    public Message consume(String topic) {
        if (!queues.containsKey(topic)) {
            throw new TopicNotFoundException(topic);
        }
        return queues.get(topic).poll();
    }
}
