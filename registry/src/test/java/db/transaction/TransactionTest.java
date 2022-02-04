package db.transaction;

import db.MemoryDb;
import db.dsl.Result;
import db.schema.RegionTable;
import db.schema.ServiceProfileTable;
import domain.Region;
import domain.ServiceProfile;
import domain.ServiceStatus;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransactionTest {

    @After
    public void tearDown() throws Exception {
        MemoryDb.instance().clear();
    }

    @Test
    public void testTransactionOk() {
        MemoryDb.instance().createTable(RegionTable.of("region-table"));
        MemoryDb.instance().createTable(ServiceProfileTable.of("profile-table"));

        Transaction transaction = Transaction.of("transaction1");
        transaction.begin();
        Command<String, Region> regionInsert = InsertCommand.<String, Region>of("region-table")
                .withPrimaryKey("0")
                .withRecord(Region.of("0").withName("region0").withCountry("China"));
        transaction.exec(regionInsert);

        ServiceProfile profile = ServiceProfile.Builder("0")
                .withEndpoint("1.1.1.1", 80)
                .withRegionId("0")
                .withStatus(ServiceStatus.NORMAL)
                .withPriority(1)
                .withLoad(100)
                .Build();
        Command<String, ServiceProfile> profileInsert = InsertCommand.<String, ServiceProfile>of("profile-table")
                .withPrimaryKey("0")
                .withRecord(profile);
        transaction.exec(profileInsert);

        try {
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Result result = MemoryDb.instance().exec("select load from profile-table where id=0");
        assertEquals(1, result.toMap().size());
        assertEquals("100", result.toMap().get("load"));

        Result result2 = MemoryDb.instance().exec("select name from region-table where id=0");
        assertEquals(1, result.toMap().size());
        assertEquals("region0", result2.toMap().get("name"));

    }

    @Test
    public void testTransactionFail() {
        MemoryDb.instance().createTable(RegionTable.of("region-table"));
        MemoryDb.instance().createTable(ServiceProfileTable.of("profile-table"));

        Transaction transaction = Transaction.of("transaction1");
        transaction.begin();
        Command<String, Region> regionInsert = InsertCommand.<String, Region>of("region-table")
                .withPrimaryKey("0")
                .withRecord(Region.of("0").withName("region0").withCountry("China"));
        transaction.exec(regionInsert);

        ServiceProfile profile = ServiceProfile.Builder("0")
                .withEndpoint("1.1.1.1", 80)
                .withRegionId("0")
                .withStatus(ServiceStatus.NORMAL)
                .withPriority(1)
                .withLoad(100)
                .Build();
        Command<String, ServiceProfile> profileUpdate = UpdateCommand.<String, ServiceProfile>of("profile-table")
                .withPrimaryKey("0")
                .withRecord(profile);
        transaction.exec(profileUpdate);

        try {
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Result result = MemoryDb.instance().exec("select name from region-table where id=0");
        assertEquals(0, result.toMap().size());
    }

}