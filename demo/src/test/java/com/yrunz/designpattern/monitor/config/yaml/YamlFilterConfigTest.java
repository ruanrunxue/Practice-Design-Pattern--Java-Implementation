package com.yrunz.designpattern.monitor.config.yaml;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YamlFilterConfigTest {

    @Test
    public void testLoad() {
        String yaml = "- name: \"filter1\"\n" +
                "  type: \"log_to_json\"\n" +
                "- name: \"filter2\"\n" +
                "  type: \"add_timestamp\"\n" +
                "- name: \"filter3\"\n" +
                "  type: \"json_to_monitor_event\"\n";
        YamlFilterConfig config = YamlFilterConfig.empty();
        config.load(yaml);
        assertEquals(3, config.items().size());
        assertEquals("filter1", config.items().get(0).name());
    }

}