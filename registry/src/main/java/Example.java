import db.MemoryDb;
import db.schma.ServiceProfileTable;

public class Example {
    public static void main(String[] args) {
        ServiceProfileTable table = ServiceProfileTable.of("ProfileTable");
        MemoryDb.instance().createTable(table);
        ServiceProfileTable pTable = (ServiceProfileTable) MemoryDb.instance().tableOf("ProfileTable");
        System.out.println(pTable);
    }
}
