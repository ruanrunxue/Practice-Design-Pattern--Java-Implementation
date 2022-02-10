package com.yrunz.designpattern.monitor.config.yaml;

import org.junit.Test;

import static org.junit.Assert.*;

public class YamlPipelineConfigTest {

    @Test
    public void testLoad() {
        String yaml = "name: \"pipeline0\"\n" +
                "type: \"single_thread\"\n" +
                "input:\n" +
                "  name: \"input0\"\n" +
                "  type: \"memory_mq\"\n" +
                "  context:\n" +
                "    topic: \"test\"\n" +
                "filter: \n" +
                "  - name: \"filter1\"\n" +
                "    type: \"log_to_json\"\n" +
                "  - name: \"filter2\"\n" +
                "    type: \"add_timestamp\"\n" +
                "  - name: \"filter3\"\n" +
                "    type: \"json_to_monitor_event\"\n" +
                "output:\n" +
                "  name: \"output0\"\n" +
                "  type: \"memory_db\"\n" +
                "  context:\n" +
                "    tableName: \"test\"\n";
        YamlPipelineConfig config = YamlPipelineConfig.of(YamlInputConfig.empty(), YamlFilterConfig.empty(), YamlOutputConfig.empty());
        config.load(yaml);
        assertEquals("pipeline0", config.name());
        assertEquals("input0", config.input().name());
        assertEquals(3, config.filter().items().size());
        assertEquals("output0", config.output().name());
    }
}