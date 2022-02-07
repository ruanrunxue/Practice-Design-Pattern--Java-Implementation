package com.yrunz.designpattern.monitor.filter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yrunz.designpattern.monitor.plugin.Event;

import java.time.LocalDateTime;
import java.time.ZoneId;

// 为ObjectNode增加timestamp字段
public class AddTimestampFilter implements FilterPlugin {
    @Override
    public Event filter(Event event) {
        // 关键点，如果不是期望的输入，则不处理，直接返回原有的
        if (!(event.payload() instanceof ObjectNode)) {
            return event;
        }
        ObjectNode jsonNode = (ObjectNode) event.payload();
        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        jsonNode.put("timestamp", now);
        return Event.of(event.header(), jsonNode);
    }

    @Override
    public void install() {

    }

    @Override
    public void uninstall() {

    }
}
