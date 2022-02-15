package com.yrunz.designpattern.monitor.filter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yrunz.designpattern.domain.MonitorEvent;
import com.yrunz.designpattern.monitor.plugin.Event;

// 将ObjectNode转换成MonitorEvent
public class Json2MonitorEventFilter implements FilterPlugin {

    @Override
    public Event filter(Event event) {
        if (!(event.payload() instanceof ObjectNode)) {
            return event;
        }
        ObjectNode jsonNode = (ObjectNode) event.payload();
        String serviceId = jsonNode.get("localIp").asText();
        String eventType = jsonNode.get("eventType").asText();
        long timestamp = jsonNode.get("timestamp").asLong();
        MonitorEvent monitorEvent = MonitorEvent.of(serviceId,
                MonitorEvent.Type.valueOf(eventType.toUpperCase()), timestamp);
        return Event.of(event.header(), monitorEvent);
    }

    @Override
    public void install() {

    }

    @Override
    public void uninstall() {

    }
}
