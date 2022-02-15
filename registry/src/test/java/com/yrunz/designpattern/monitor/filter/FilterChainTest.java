package com.yrunz.designpattern.monitor.filter;

import com.yrunz.designpattern.domain.MonitorEvent;
import com.yrunz.designpattern.monitor.config.json.JsonFilterConfig;
import com.yrunz.designpattern.monitor.plugin.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterChainTest {

    @Test
    public void testInstance() {
        String json = "[{\"name\":\"log_to_json_0\", \"type\":\"log_to_json\"}," +
                "{\"name\":\"add_timestamp_0\", \"type\":\"add_timestamp\"}," +
                "{\"name\":\"json_to_monitor_event_0\", \"type\":\"json_to_monitor_event\"}]";
        JsonFilterConfig config = JsonFilterConfig.empty();
        config.load(json);

        FilterPlugin filterPlugin = FilterPluginFactory.newInstance().create(config);
        assertTrue(filterPlugin instanceof FilterChain);
    }

    @Test
    public void testFilter() {
        String json = "[{\"name\":\"log_to_json_0\", \"type\":\"log_to_json\"}," +
                "{\"name\":\"add_timestamp_0\", \"type\":\"add_timestamp\"}," +
                "{\"name\":\"json_to_monitor_event_0\", \"type\":\"json_to_monitor_event\"}]";
        JsonFilterConfig config = JsonFilterConfig.empty();
        config.load(json);
        FilterChain filterPlugin = (FilterChain) FilterPluginFactory.newInstance().create(config);
        filterPlugin.install();

        Event event = Event.of("[192.168.1.1][recv_req]receive request from address 192.168.1.91 success");
        event = filterPlugin.filter(event);

        assertTrue(event.payload() instanceof MonitorEvent);
        MonitorEvent monitorEvent = (MonitorEvent) event.payload();
        assertEquals("192.168.1.1", monitorEvent.serviceId());
        assertEquals(MonitorEvent.Type.RECV_REQ, monitorEvent.eventType());
    }

}