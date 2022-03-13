package com.yrunz.designpattern.db.cache;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.service.registry.model.schema.RegionTable;
import com.yrunz.designpattern.service.registry.model.Region;
import org.junit.After;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class CacheDbProxyTest {

    @After
    public void tearDown() {
        MemoryDb.instance().clear();
    }

    @Test
    public void testCache_0() {
        CacheDbProxy db = CacheDbProxy.of(MemoryDb.instance());
        db.createTable(RegionTable.of("test"));
        Region region = Region.of("0").withCountry("China").withName("region-0");
        db.insert("test", "0", region);

        Optional<Region> record = db.query("test", "0");
        assertTrue(record.isPresent());
        assertEquals(1, db.cacheHitCount());
        record = db.query("test", "0");
        assertTrue(record.isPresent());
        assertEquals(2, db.cacheHitCount());
    }

    @Test
    public void testCache_1() {
        MemoryDb.instance().createTable(RegionTable.of("test"));
        Region region = Region.of("0").withCountry("China").withName("region-0");
        MemoryDb.instance().insert("test", "0", region);

        CacheDbProxy proxy = CacheDbProxy.of(MemoryDb.instance());
        Optional<Region> record = proxy.query("test", "0");
        assertTrue(record.isPresent());
        assertEquals(0, proxy.cacheHitCount());
        assertEquals(1, proxy.cacheMissCount());
        record = proxy.query("test", "0");
        assertTrue(record.isPresent());
        assertEquals(1, proxy.cacheHitCount());


    }

}