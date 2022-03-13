package com.yrunz.designpattern.db;

import com.yrunz.designpattern.db.dsl.Result;
import com.yrunz.designpattern.service.registry.model.schema.RegionTable;
import com.yrunz.designpattern.service.registry.model.schema.SubscriptionTable;
import com.yrunz.designpattern.service.registry.model.Region;
import com.yrunz.designpattern.service.registry.model.Subscription;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class MemoryDbTest {

    @After
    public void tearDown() {
        MemoryDb.instance().clear();
    }

    @Test
    public void testRegionTable() {
        MemoryDb.instance().createTable(RegionTable.of("TestRegionTable"));
        Region region = Region.of("0").withName("region-0").withCountry("China");
        MemoryDb.instance().insert("TestRegionTable", region.id(), region);

        Optional<Region> region2 = MemoryDb.instance().query("TestRegionTable", "0");
        Assert.assertTrue(region2.isPresent());
        Assert.assertEquals("region-0", region2.get().name());
        Assert.assertEquals("China", region2.get().country());

        MemoryDb.instance().update("TestRegionTable", "0", Region.of("0").withName("region-1").withCountry("China"));
        Optional<Region> region3 = MemoryDb.instance().query("TestRegionTable","0");
        Assert.assertTrue(region3.isPresent());
        Assert.assertEquals("region-1", region3.get().name());
        Assert.assertEquals("China", region3.get().country());

        MemoryDb.instance().delete("TestRegionTable", "0");
        Optional<Region> region4 = MemoryDb.instance().query("TestRegionTable","0");
        Assert.assertFalse(region4.isPresent());
    }

    @Test
    public void testDsl() {
        SubscriptionTable table = SubscriptionTable.of("TestTable");
        table.insert("0", Subscription.create().withId("0").withSrcServiceId("src1").withTargetServiceId("target1"));
        table.insert("1", Subscription.create().withId("1").withSrcServiceId("src1").withTargetServiceId("target2"));
        table.insert("2", Subscription.create().withId("2").withSrcServiceId("src2").withTargetServiceType("svc1"));
        table.insert("3", Subscription.create().withId("3").withSrcServiceId("src2").withTargetServiceId("target1"));

        MemoryDb.instance().createTable(table);

        Result result = MemoryDb.instance().exec("select srcServiceId from TestTable where subscriptionId=0");
        Assert.assertEquals(1, result.toMap().size());
        Assert.assertEquals("src1", result.toMap().get("srcServiceId"));

        result = MemoryDb.instance().exec("select srcServiceId,targetServiceId,targetServiceType from TestTable where subscriptionId=1");
        Assert.assertEquals(3, result.toMap().size());
        Assert.assertEquals("src1", result.toMap().get("srcServiceId"));
        Assert.assertEquals("target2", result.toMap().get("targetServiceId"));
        Assert.assertEquals("", result.toMap().get("targetServiceType"));
    }

}