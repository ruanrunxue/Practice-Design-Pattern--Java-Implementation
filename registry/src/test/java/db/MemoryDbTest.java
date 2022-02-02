package db;

import db.schema.RegionTable;
import domain.Region;
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
        Region region = Region.of(0).withName("region-0").withCountry("China");
        table.insert(region.id(), region);

        RegionTable table2 = (RegionTable) MemoryDb.instance().tableOf("TestRegionTable");
        Optional<Region> region2 = table2.query(0);
        Assert.assertTrue(region2.isPresent());
        Assert.assertEquals("region-0", region2.get().name());
        Assert.assertEquals("China", region2.get().country());

        table.update(0, Region.of(0).withName("region-1").withCountry("China"));
        Optional<Region> region3 = table.query(0);
        Assert.assertTrue(region3.isPresent());
        Assert.assertEquals("region-1", region3.get().name());
        Assert.assertEquals("China", region3.get().country());

        table.delete(0);
        Optional<Region> region4 = table.query(0);
        Assert.assertFalse(region4.isPresent());
    }



}