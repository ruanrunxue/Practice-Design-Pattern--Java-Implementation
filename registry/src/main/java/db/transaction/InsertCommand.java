package db.transaction;

import db.MemoryDb;
import db.Table;

public class InsertCommand <PrimaryKey, Record> implements Command<PrimaryKey, Record> {
    private final String tableName;
    private PrimaryKey primaryKey;
    private Record record;

    private InsertCommand(String tableName) {
        this.tableName = tableName;
    }

    public static <PrimaryKey, Record> InsertCommand<PrimaryKey, Record> of(String tableName) {
        return new InsertCommand<>(tableName);
    }

    public InsertCommand<PrimaryKey, Record> withPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public InsertCommand<PrimaryKey, Record> withRecord(Record record) {
        this.record = record;
        return this;
    }

    @Override
    public void exec() {
        Table<PrimaryKey, Record> table = (Table<PrimaryKey, Record>) MemoryDb.instance().tableOf(tableName);
        table.insert(primaryKey, record);
    }

    @Override
    public void undo() {
        Table<PrimaryKey, Record> table = (Table<PrimaryKey, Record>) MemoryDb.instance().tableOf(tableName);
        table.delete(primaryKey);
    }

}
