package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.config.json.JsonInputConfig;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.mq.Message;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryMqInputTest {

    @Test
    public void testInstance() {
        String json = "{\"name\":\"memory_mq_0\", \"type\":\"memory_mq\", \"context\":{\"topic\":\"test\"}}";
        JsonInputConfig config = JsonInputConfig.empty();
        config.load(json);
        assertEquals("memory_mq_0", config.name());
        assertEquals(InputType.MEMORY_MQ, config.type());

        InputPlugin inputPlugin = InputPluginFactory.newInstance().create(config);
        assertNotNull(inputPlugin);

        assertTrue(inputPlugin instanceof MemoryMqInput);
    }

    @Test
    public void testInput() {
        String json = "{\"name\":\"memory_mq_0\", \"type\":\"memory_mq\", \"context\":{\"topic\":\"test\"}}";
        JsonInputConfig config = JsonInputConfig.empty();
        config.load(json);
        MemoryMqInput inputPlugin = (MemoryMqInput) InputPluginFactory.newInstance().create(config);
        inputPlugin.install();

        MemoryMq.instance().produce(Message.of("test", "hello world"));
        Event event = inputPlugin.input();
        assertEquals("test", event.header().get("topic"));
        assertTrue(event.payload() instanceof String);
        assertEquals("hello world", event.payload());
    }

}