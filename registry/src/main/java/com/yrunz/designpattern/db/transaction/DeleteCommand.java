package com.yrunz.designpattern.db.transaction;

import com.yrunz.designpattern.db.Db;

import java.util.Optional;

public class DeleteCommand<PrimaryKey, Record> implements Command {
    private Db db;
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
        Optional<Record> oldRecord = db.query(tableName, primaryKey);
        oldRecord.ifPresent(record -> this.oldRecord = record);
        db.delete(tableName, primaryKey);
    }

    @Override
    public void undo() {
        db.insert(tableName, primaryKey, oldRecord);
    }

    @Override
    public void setDb(Db db) {
        this.db = db;
    }
}
