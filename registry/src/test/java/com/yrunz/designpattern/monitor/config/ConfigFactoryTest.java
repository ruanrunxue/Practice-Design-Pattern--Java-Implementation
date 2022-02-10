package com.yrunz.designpattern.monitor.config;

import com.yrunz.designpattern.monitor.config.json.JsonConfigFactory;
import com.yrunz.designpattern.monitor.config.json.JsonPipelineConfig;
import com.yrunz.designpattern.monitor.config.yaml.YamlConfigFactory;
import com.yrunz.designpattern.monitor.config.yaml.YamlPipelineConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigFactoryTest {

    @Test
    public void testJsonConfigFactory() {
        ConfigFactory factory = JsonConfigFactory.newInstance();
        PipelineConfig config = factory.createPipelineConfig();
        assertTrue(config instanceof JsonPipelineConfig);
    }

    @Test
    public void testYamlConfigFactory() {
        ConfigFactory factory = YamlConfigFactory.newInstance();
        PipelineConfig config = factory.createPipelineConfig();
        assertTrue(config instanceof YamlPipelineConfig);

    }
}