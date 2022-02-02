import db.MemoryDb;
import db.Table;
import db.schma.ProfileTable;
import domain.ServiceProfile;

import java.util.UUID;

public class Example {
    public static void main(String[] args) {
        ProfileTable table = ProfileTable.empty();
        MemoryDb.instance().createTable(table);
        ProfileTable pTable = (ProfileTable) MemoryDb.instance().tableOf("ProfileTable");
        System.out.println(pTable.name());
    }
}
