package com.yrunz.designpattern.db.cache;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.db.dsl.Result;
import com.yrunz.designpattern.db.transaction.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 代理模式
 */

// 增加Db缓存
public class CacheDbProxy implements Db {

    private final Db db;
    private final Map<String, Map<Object, Object>> caches;

    private int cacheHitCount;
    private int cacheMissCount;

    private CacheDbProxy(Db db) {
        this.db = db;
        this.caches = new HashMap<>();
        this.cacheHitCount = 0;
        this.cacheMissCount = 0;
    }

    public static CacheDbProxy of(Db db) {
        return new CacheDbProxy(db);
    }

    // 首次获取时
    @Override
    public <PrimaryKey, Record> Optional<Record> query(String tableName, PrimaryKey primaryKey) {
        Map<Object, Object> cache = caches.get(tableName);
        if (cache != null && cache.containsKey(primaryKey)) {
            cacheHit();
            return Optional.of((Record) cache.get(primaryKey));
        }
        cacheMiss();
        Optional<Record> record = db.query(tableName, primaryKey);
        if (record.isPresent()) {
            caches.putIfAbsent(tableName, new HashMap<>());
            caches.get(tableName).put(primaryKey, record);
        }
        return record;
    }

    @Override
    public <PrimaryKey, Record> void insert(String tableName, PrimaryKey primaryKey, Record record) {
        Map<Object, Object> cache = caches.get(tableName);
        if (cache != null) {
            cache.putIfAbsent(primaryKey, record);
        }
        db.insert(tableName, primaryKey, record);
    }

    @Override
    public <PrimaryKey, Record> void update(String tableName, PrimaryKey primaryKey, Record record) {
        Map<Object, Object> cache = caches.get(tableName);
        if (cache != null) {
            if (cache.containsKey(primaryKey)) {
                cache.put(primaryKey, record);
            }
        }
        db.update(tableName, primaryKey, record);
    }

    @Override
    public <PrimaryKey> void delete(String tableName, PrimaryKey primaryKey) {
        Map<Object, Object> cache = caches.get(tableName);
        if (cache != null) {
            cache.remove(primaryKey);
        }
        db.delete(tableName, primaryKey);
    }

    @Override
    public <Record> List<Record> accept(String tableName, TableVisitor<Record> visitor) {
        return db.accept(tableName, visitor);
    }

    @Override
    public void createTable(Table<?, ?> table) {
        caches.putIfAbsent(table.name(), new HashMap<>());
        db.createTable(table);
    }

    @Override
    public void deleteTable(String tableName) {
        caches.remove(tableName);
        db.deleteTable(tableName);
    }

    @Override
    public void createTableIfNotExist(Table<?, ?> table) {
            db.createTableIfNotExist(table);
    }

    @Override
    public Transaction createTransaction(String transactionName) {
        return db.createTransaction(transactionName);
    }

    @Override
    public Result exec(String dslExpression) {
        return db.exec(dslExpression);
    }

    private void cacheHit() {
        cacheHitCount++;
    }

    private void cacheMiss() {
        cacheMissCount++;
    }

    public int cacheHitCount() {
        return cacheHitCount;
    }

    public int cacheMissCount() {
        return cacheMissCount;
    }
}
