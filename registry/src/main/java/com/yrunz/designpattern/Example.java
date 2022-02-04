package com.yrunz.designpattern;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.cmd.Cmd;
import com.yrunz.designpattern.db.schema.SubscriptionTable;
import com.yrunz.designpattern.domain.Subscription;

public class Example {
    public static void main(String[] args) {
        SubscriptionTable table = SubscriptionTable.of("TestTable");
        table.insert("0", Subscription.of("0").withSrcServiceId("src1").withTargetServiceId("target1"));
        table.insert("1", Subscription.of("1").withSrcServiceId("src1").withTargetServiceId("target2"));
        table.insert("2", Subscription.of("2").withSrcServiceId("src2").withTargetServiceType("svc1"));
        table.insert("3", Subscription.of("3").withSrcServiceId("src2").withTargetServiceId("target1"));

        MemoryDb.instance().createTable(table);

        Cmd.create().start();
    }
}
