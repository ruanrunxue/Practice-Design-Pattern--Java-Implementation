package com.yrunz.designpattern.monitor;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.monitor.schema.MonitorEventVisitor;
import com.yrunz.designpattern.monitor.entity.MonitorEvent;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.mq.Message;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MonitorSystemTest {

    @Test
    public void testStart() throws InterruptedException {
        MemoryMq.instance().createTopic("monitor_0");
        MemoryMq.instance().produce(Message.of("monitor_0", "[service0][recv_req]receive request from address 192.168.1.91 success"));
        Thread.sleep(1000);

        MonitorSystem monitorSystem = MonitorSystem.of("src/test/resources/pipelines");
        monitorSystem.start();
        Thread.sleep(1000);


        MonitorEventVisitor visitor = MonitorEventVisitor.create().withServiceId("service0");
        List<MonitorEvent> records = MemoryDb.instance().accept("monitor_event_0", visitor);
        assertEquals(1, records.size());
    }

}