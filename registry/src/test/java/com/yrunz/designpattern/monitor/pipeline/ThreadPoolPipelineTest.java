package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.db.MemoryDb;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadPoolPipelineTest {

    @After
    public void tearDown() throws Exception {
        MemoryDb.instance().clear();
    }

    @Test
    public void testInstance() {
        String json = "{\"name\":\"pipeline0\", \"type\":\"thread_pool\", " +
                "\"input\":{\"name\":\"memory_mq_0\", \"type\":\"memory_mq\", \"context\":{\"topic\":\"test\"}}," +
                "\"output\":{\"name\":\"memory_db_0\", \"type\":\"memory_db\", \"context\":{\"tableName\":\"test\"}}," +
                "\"filter\":" +"[{\"name\":\"log_to_json_0\", \"type\":\"log_to_json\"}," +
                "{\"name\":\"add_timestamp_0\", \"type\":\"add_timestamp\"}," +
                "{\"name\":\"json_to_monitor_event_0\", \"type\":\"json_to_monitor_event\"}]" +"}";
        PipelineJsonConfig config = PipelineJsonConfig.empty();
        config.load(json);

        Pipeline pipeline = PipelineFactory.newInstance().create(config);
        assertTrue(pipeline instanceof ThreadPoolPipeline);
    }

    @Test
    public void testInstall() {
        String json = "{\"name\":\"pipeline0\", \"type\":\"thread_pool\", " +
                "\"input\":{\"name\":\"memory_mq_0\", \"type\":\"memory_mq\", \"context\":{\"topic\":\"test\"}}," +
                "\"output\":{\"name\":\"memory_db_0\", \"type\":\"memory_db\", \"context\":{\"tableName\":\"test\"}}," +
                "\"filter\":" +"[{\"name\":\"log_to_json_0\", \"type\":\"log_to_json\"}," +
                "{\"name\":\"add_timestamp_0\", \"type\":\"add_timestamp\"}," +
                "{\"name\":\"json_to_monitor_event_0\", \"type\":\"json_to_monitor_event\"}]" +"}";
        PipelineJsonConfig config = PipelineJsonConfig.empty();
        config.load(json);

        Pipeline pipeline = PipelineFactory.newInstance().create(config);
        pipeline.install();
    }


}