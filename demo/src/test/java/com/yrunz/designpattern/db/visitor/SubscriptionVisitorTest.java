package com.yrunz.designpattern.db.visitor;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.schema.SubscriptionTable;
import com.yrunz.designpattern.domain.Subscription;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SubscriptionVisitorTest {

    @After
    public void tearDown() throws Exception {
        MemoryDb.instance().clear();
    }

    @Test
    public void testSubscriptionVisitor() {
        SubscriptionTable table = SubscriptionTable.of("TestTable");
        table.insert("0", Subscription.create().withId("0").withSrcServiceId("src1").withTargetServiceId("target1"));
        table.insert("1", Subscription.create().withId("1").withSrcServiceId("src1").withTargetServiceId("target2"));
        table.insert("2", Subscription.create().withId("2").withSrcServiceId("src2").withTargetServiceType("svc1"));
        table.insert("3", Subscription.create().withId("3").withSrcServiceId("src2").withTargetServiceId("target1"));

        SubscriptionVisitor visitor1 = SubscriptionVisitor.create()
                .withTargetServiceId("target1")
                .withTargetServiceType("svc1");
        List<Subscription> result1 = table.accept(visitor1);
        assertEquals(3, result1.size());

        SubscriptionVisitor visitor2 = SubscriptionVisitor.create()
                .withTargetServiceId("target2")
                .withTargetServiceType("svc2");
        List<Subscription> result2 = table.accept(visitor2);
        assertEquals(1, result2.size());
    }
}