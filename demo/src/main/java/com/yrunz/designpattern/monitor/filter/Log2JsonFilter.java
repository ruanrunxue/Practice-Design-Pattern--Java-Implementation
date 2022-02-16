package com.yrunz.designpattern.monitor.filter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yrunz.designpattern.monitor.plugin.Event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 将日志转换为JsonNode对象，其中日志的格式为：[localIp][EventType]xxxxx
// 举例[192.168.1.1][recv_req]receive request from address 192.168.1.91 success
public class Log2JsonFilter implements FilterPlugin {

    private final Pattern logPattern;

    public Log2JsonFilter() {
        logPattern = Pattern.compile("\\[(.+)]\\[(.+)].*");
    }

    @Override
    public Event filter(Event event) {
        if (!(event.payload() instanceof String)) {
            return event;
        }
        String log = (String) event.payload();
        Matcher matcher = logPattern.matcher(log);
        if (!matcher.matches()) {
            return event;
        }
        ObjectNode logJson = new ObjectNode(JsonNodeFactory.instance);
        logJson.put("localIp", matcher.group(1));
        logJson.put("eventType", matcher.group(2));
        return Event.of(event.header(), logJson);
    }

    @Override
    public void install() {

    }

    @Override
    public void uninstall() {

    }
}
