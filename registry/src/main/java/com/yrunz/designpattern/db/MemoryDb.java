package com.yrunz.designpattern.db;

import com.yrunz.designpattern.db.dsl.CompoundExpression;
import com.yrunz.designpattern.db.dsl.Context;
import com.yrunz.designpattern.db.dsl.Result;
import com.yrunz.designpattern.db.exception.TableAlreadyExistException;
import com.yrunz.designpattern.db.exception.TableNotFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 单例模式
 * 主要用于保证一个类仅有一个实例，并提供一个访问它的全局访问点。
 * 在对某个对象实现单例模式时，有两个点必须要注意：
 * （1）限制调用者直接实例化该对象；
 * （2）为该对象的单例提供一个全局唯一的访问方法；
 * 单例模式具体又可分为两种：饿汉模式和懒汉模式
 * （1）饿汉模式：实例在系统加载的时候就已经完成了初始化，实现简单。
 * （2）懒汉模式：等到对象被使用的时候，才会去初始化它，从而一定程度上节省了内存。普通的懒汉模式有线程安全问题，
 * 可以通过普通加锁，或者更高效的双重检验锁来解决线程安全问题
 */

//  饿汉单例模式
public class MemoryDb {
    // 关键点1：定义全局唯一的实例，系统初始化时就已经完成实例化
    private static final MemoryDb INSTANCE = new MemoryDb();

    private final Map<String, Table<?, ?>> tables;

    // 关键点2：私有构造函数，防止使用者实例化
    private MemoryDb() {
        tables = new HashMap<>();
    }

    // 关键点3：给使用者提供唯一点单例访问方法
    public static MemoryDb instance() {
        return INSTANCE;
    }

    // 查找表，如果表不存在，则抛出TableNotFoundException
    public Table<?, ?> tableOf(String tableName) {
        if (!tables.containsKey(tableName.toLowerCase())) {
            throw new TableNotFoundException(tableName.toLowerCase());
        }
        return tables.get(tableName.toLowerCase());
    }

    // 创建表，如果表名已经创建，则抛出TableAlreadyExistException
    public void createTable(Table<?, ?> table) {
        if (tables.containsKey(table.name().toLowerCase())) {
            throw new TableAlreadyExistException(table.name().toLowerCase());
        }
        tables.put(table.name().toLowerCase(), table);
    }

    // 删除表，如果表不存在，则抛出TableNotFoundException
    public void deleteTable(String tableName) {
        if (!tables.containsKey(tableName.toLowerCase())) {
            throw new TableNotFoundException(tableName.toLowerCase());
        }
        tables.remove(tableName.toLowerCase());
    }

    // 删除所有表
    public void clear() {
        tables.clear();
    }

    // 执行DSL语句，当前只支持select查询
    // 例子：select regionId from regionTable where regionId=1
    public Result exec(String dslExpression) {
        Context context = Context.create();
        CompoundExpression.of(dslExpression).interpret(context);

        Table table = tableOf(context.tableName());
        Optional record = table.query(context.primaryKeyValue());
        if (!record.isPresent()) {
            return Result.empty();
        }
        Result result = Result.empty();
        // 值返回需要的字段
        for (String column : context.columns()) {
            try {
                Object r = record.get();
                // 通过反射getDeclaredField方法获取私有字段
                Field field = r.getClass().getDeclaredField(column);
                // 必须设置为true才能得到字段值
                field.setAccessible(true);
                Object value = field.get(r);
                result.add(column, value.toString());
            } catch (Exception e) {
                result.add(column, "NA");
            }
        }
        return result;
    }
}
