package com.yrunz.designpattern.db;

import com.yrunz.designpattern.db.transaction.Transaction;

import java.util.List;
import java.util.Optional;

// DB抽象接口
public interface Db {
    <PrimaryKey, Record> Optional<Record> query(String tableName, PrimaryKey primaryKey);
    <PrimaryKey, Record> void insert(String tableName, PrimaryKey primaryKey, Record record);
    <PrimaryKey, Record> void update(String tableName, PrimaryKey primaryKey, Record record);
    <PrimaryKey> void delete(String tableName, PrimaryKey primaryKey);
    <Record> List<Record> accept(String tableName, TableVisitor<Record> visitor);
    void createTable(Table<?, ?> table);
    void deleteTable(String tableName);
    Transaction createTransaction(String transactionName);
}
