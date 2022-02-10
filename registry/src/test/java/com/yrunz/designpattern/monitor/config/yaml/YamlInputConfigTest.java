package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.input.InputType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YamlInputConfigTest {

    @Test
    public void testLoad() {
        String yaml = "name: \"input1\"\n" +
                "type: \"memory_mq\"\n" +
                "context:\n" +
                "  topic: \"test\"";
        YamlInputConfig config = YamlInputConfig.empty();
        config.load(yaml);
        assertEquals("input1", config.name());
        Assert.assertEquals(InputType.MEMORY_MQ, config.type());
        assertEquals("test", config.context(). getString("topic"));
    }

}