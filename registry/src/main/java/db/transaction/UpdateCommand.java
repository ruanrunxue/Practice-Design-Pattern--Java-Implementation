package db.transaction;

import db.MemoryDb;
import db.Table;

import java.util.Optional;

public class UpdateCommand<PrimaryKey, Record> implements Command<PrimaryKey, Record> {
    private final String tableName;
    private PrimaryKey primaryKey;
    private Record newRecord;
    private Record oldRecord;

    private UpdateCommand(String tableName) {
        this.tableName = tableName;
    }

    public static <PrimaryKey, Record> UpdateCommand<PrimaryKey, Record> of(String tableName) {
        return new UpdateCommand<>(tableName);
    }

    public UpdateCommand<PrimaryKey, Record> withPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    public UpdateCommand<PrimaryKey, Record> withRecord(Record record) {
        this.newRecord = record;
        return this;
    }

    @Override
    public void exec() {
        Table<PrimaryKey, Record> table = (Table<PrimaryKey, Record>) MemoryDb.instance().tableOf(tableName);
        Optional<Record> oldRecord = table.query(primaryKey);
        oldRecord.ifPresent(record -> this.oldRecord = record);
        table.update(primaryKey, newRecord);
    }

    @Override
    public void undo() {
        Table<PrimaryKey, Record> table = (Table<PrimaryKey, Record>) MemoryDb.instance().tableOf(tableName);
        table.update(primaryKey, oldRecord);
    }
}
