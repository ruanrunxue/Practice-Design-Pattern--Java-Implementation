package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.output.OutputType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YamlOutputConfigTest {

    @Test
    public void testLoad() {
        String yaml = "name: \"output1\"\n" +
                "type: \"memory_db\"\n" +
                "context:\n" +
                "  tableName: \"test\"";
        YamlOutputConfig config = YamlOutputConfig.empty();
        config.load(yaml);

        assertEquals("output1", config.name());
        Assert.assertEquals(OutputType.MEMORY_DB, config.type());
        assertEquals("test", config.context().getString("tableName"));
    }

}