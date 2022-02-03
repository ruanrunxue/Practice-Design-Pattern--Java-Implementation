package db;

import db.dsl.Result;
import db.schema.RegionTable;
import db.schema.SubscriptionTable;
import domain.Region;
import domain.Subscription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class MemoryDbTest {

    @Before
    public void setUp() throws Exception {
        MemoryDb.instance().clear();
    }

    @Test
    public void testRegionTable() {
        MemoryDb.instance().createTable(RegionTable.of("TestRegionTable"));
        RegionTable table = (RegionTable) MemoryDb.instance().tableOf("TestRegionTable");
        Assert.assertEquals("TestRegionTable", table.name());
        Region region = Region.of("0").withName("region-0").withCountry("China");
        table.insert(region.id(), region);

        RegionTable table2 = (RegionTable) MemoryDb.instance().tableOf("TestRegionTable");
        Optional<Region> region2 = table2.query("0");
        Assert.assertTrue(region2.isPresent());
        Assert.assertEquals("region-0", region2.get().name());
        Assert.assertEquals("China", region2.get().country());

        table.update("0", Region.of("0").withName("region-1").withCountry("China"));
        Optional<Region> region3 = table.query("0");
        Assert.assertTrue(region3.isPresent());
        Assert.assertEquals("region-1", region3.get().name());
        Assert.assertEquals("China", region3.get().country());

        table.delete("0");
        Optional<Region> region4 = table.query("0");
        Assert.assertFalse(region4.isPresent());
    }

    @Test
    public void testDsl() {
        SubscriptionTable table = SubscriptionTable.of("TestTable");
        table.insert("0", Subscription.of("0").withSrcServiceId("src1").withTargetServiceId("target1"));
        table.insert("1", Subscription.of("1").withSrcServiceId("src1").withTargetServiceId("target2"));
        table.insert("2", Subscription.of("2").withSrcServiceId("src2").withTargetServiceType("svc1"));
        table.insert("3", Subscription.of("3").withSrcServiceId("src2").withTargetServiceId("target1"));

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