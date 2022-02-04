package db.transaction;

import db.MemoryDb;
import db.Table;

import java.util.Optional;

public class DeleteCommand<PrimaryKey, Record> implements Command<PrimaryKey, Record> {
    private final String tableName;
    private PrimaryKey primaryKey;
    private Record oldRecord;

    private DeleteCommand(String tableName) {
        this.tableName = tableName;
    }

    public static <PrimaryKey, Record> DeleteCommand<PrimaryKey, Record> of(String tableName) {
        return new DeleteCommand<>(tableName);
    }

    public DeleteCommand<PrimaryKey, Record> withPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
        return this;
    }

    @Override
    public void exec() {
        Table<PrimaryKey, Record> table = (Table<PrimaryKey, Record>) MemoryDb.instance().tableOf(tableName);
        Optional<Record> oldRecord = table.query(primaryKey);
        oldRecord.ifPresent(record -> this.oldRecord = record);
        table.delete(primaryKey);
    }

    @Override
    public void undo() {
        Table<PrimaryKey, Record> table = (Table<PrimaryKey, Record>) MemoryDb.instance().tableOf(tableName);
        table.insert(primaryKey, oldRecord);
    }
}
