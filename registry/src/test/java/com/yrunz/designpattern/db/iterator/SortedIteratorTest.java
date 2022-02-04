package com.yrunz.designpattern.db.iterator;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.schema.RegionTable;
import com.yrunz.designpattern.domain.Region;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedIteratorTest {

    @After
    public void tearDown() throws Exception {
        MemoryDb.instance().clear();
    }

    @Test
    public void testRegionTableIterator() {
        RegionTable table = RegionTable.of("TestRegion");
        table.insert("0", Region.of("0").withName("region-0").withCountry("China"));
        table.insert("3", Region.of("3").withName("region-3").withCountry("USA"));
        table.insert("2", Region.of("2").withName("region-2").withCountry("US"));

        TableIterator<Region> iterator = table.iterator();
        String[] expects = new String[] {"0", "2", "3"};
        int i = 0;
        while (iterator.hasNext()) {
            Region region = iterator.next();
            assertEquals(expects[i++], region.id());
        }
    }

}