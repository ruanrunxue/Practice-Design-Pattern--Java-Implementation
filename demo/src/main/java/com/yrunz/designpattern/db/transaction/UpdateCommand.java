package com.yrunz.designpattern.db.transaction;

import com.yrunz.designpattern.db.Db;

import java.util.Optional;

public class UpdateCommand<PrimaryKey, Record> implements Command {
    private Db db;
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
        Optional<Record> oldRecord = db.query(tableName, primaryKey);
        oldRecord.ifPresent(record -> this.oldRecord = record);
        db.update(tableName, primaryKey, newRecord);
    }

    @Override
    public void undo() {
        db.update(tableName, primaryKey, oldRecord);
    }

    @Override
    public void setDb(Db db) {
        this.db = db;
    }
}
