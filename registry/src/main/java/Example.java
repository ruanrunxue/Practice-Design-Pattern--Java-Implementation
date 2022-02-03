import db.MemoryDb;
import db.Table;
import db.schema.ServiceProfileTable;

public class Example {
    public static void main(String[] args) {
        ServiceProfileTable table = ServiceProfileTable.of("ProfileTable");
        MemoryDb.instance().createTable(table);
        ServiceProfileTable pTable = (ServiceProfileTable) MemoryDb.instance().tableOf("ProfileTable");
        System.out.println(pTable);
    }
}
