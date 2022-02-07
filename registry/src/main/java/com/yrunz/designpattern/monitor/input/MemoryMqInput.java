package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.plugin.Config;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.mq.Message;
import com.yrunz.designpattern.mq.TopicAlreadyExistException;

import java.util.HashMap;
import java.util.Map;

// 从MemoryMq上消费数据
public class MemoryMqInput implements InputPlugin {

    private String topic;

    @Override
    public Event input() {
        Message message = MemoryMq.instance().consume(topic);
        Map<String, String> header = new HashMap<>();
        header.put("topic", topic);
        return Event.of(header, message.payload());
    }

    @Override
    public void setContext(Config.Context context) {
        this.topic = context.getString("topic");
    }


    @Override
    public void install() {
        try {
            MemoryMq.instance().createTopic(topic);
        } catch (TopicAlreadyExistException e) {
            // 如果已经创建过，则不再创建
        }
    }

    @Override
    public void uninstall() {

    }
}
