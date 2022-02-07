package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.schema.MonitorEventTable;
import com.yrunz.designpattern.domain.MonitorEvent;
import com.yrunz.designpattern.monitor.plugin.Event;
import org.junit.After;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.Assert.*;

public class MemoryDbOutputTest {

    @After
    public void tearDown() throws Exception {
        MemoryDb.instance().clear();
    }

    @Test
    public void testInstance() {
        String json = "{\"name\":\"memory_db_0\", \"type\":\"memory_db\", \"context\":{\"tableName\":\"test\"}}";
        OutputJsonConfig config = OutputJsonConfig.empty();
        config.load(json);
        assertEquals("memory_db_0", config.name());
        assertEquals(OutputType.MEMORY_DB, config.type());

        OutputPlugin outputPlugin = OutputPluginFactory.newInstance().create(config);
        assertNotNull(outputPlugin);

        assertTrue(outputPlugin instanceof MemoryDbOutput);
    }

    @Test
    public void testOutput() {
        String json = "{\"name\":\"memory_db_0\", \"type\":\"memory_db\", \"context\":{\"tableName\":\"test\"}}";
        OutputJsonConfig config = OutputJsonConfig.empty();
        config.load(json);
        MemoryDbOutput outputPlugin = (MemoryDbOutput) OutputPluginFactory.newInstance().create(config);
        outputPlugin.install();

        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        MonitorEvent event = MonitorEvent.of("0", "service0", MonitorEvent.Type.RECV_REQ, now);
        outputPlugin.output(Event.of(event));

        MonitorEventTable table = (MonitorEventTable) MemoryDb.instance().tableOf("test");
        Optional<MonitorEvent> record = table.query("0");
        assertTrue(record.isPresent());
        assertEquals("service0", record.get().serviceId());
    }

}