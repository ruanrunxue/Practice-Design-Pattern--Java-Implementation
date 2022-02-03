package db.iterator;

import db.TableIterator;
import db.schema.RegionTable;
import domain.Region;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedIteratorTest {

    @Test
    public void testRegionTableIterator() {
        RegionTable table = RegionTable.of("TestRegion");
        table.insert(0, Region.of(0).withName("region-0").withCountry("China"));
        table.insert(3, Region.of(3).withName("region-3").withCountry("USA"));
        table.insert(2, Region.of(2).withName("region-2").withCountry("US"));

        TableIterator<Region> iterator = table.iterator();
        int[] expects = new int[] {0, 2, 3};
        int i = 0;
        while (iterator.hasNext()) {
            Region region = iterator.next();
            assertEquals(expects[i++], region.id());
        }
    }

}