package com.yrunz.designpattern.db.transaction;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.Table;

public class InsertCommand <PrimaryKey, Record> implements Command {
    private Db db;
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
    public void setDb(Db db) {
        this.db = db;
    }

    @Override
    public void exec() {
        db.insert(tableName, primaryKey, record);
    }

    @Override
    public void undo() {
        db.delete(tableName, primaryKey);
    }

}
