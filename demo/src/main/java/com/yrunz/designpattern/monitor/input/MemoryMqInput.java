package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.mq.Message;
import com.yrunz.designpattern.mq.Consumable;

import java.util.HashMap;
import java.util.Map;

// 从MemoryMq上消费数据
public class MemoryMqInput implements InputPlugin {

    private String topic;
    private Consumable consumer;

    @Override
    public Event input() {
        Message message = consumer.consume(topic);
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
        consumer = MemoryMq.instance();
    }

    @Override
    public void uninstall() {

    }
}
