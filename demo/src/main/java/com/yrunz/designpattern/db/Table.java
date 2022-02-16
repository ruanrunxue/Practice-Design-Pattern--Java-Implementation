package com.yrunz.designpattern.db;

import java.util.List;
import java.util.Optional;

// 数据表接口定义，创建的数据表需要实现该接口
// PrimaryKey: 主键类型
// Record：表记录类型
public interface Table<PrimaryKey, Record> {
    // 返回表名，表名在整个数据库中唯一
    String name();

    // 根据主键key查询表记录
    Optional<Record> query(PrimaryKey key);

    // 插入表记录
    void insert(PrimaryKey key, Record record);

    // 更新表记录，newRecord为新的记录
    void update(PrimaryKey key, Record newRecord);

    // 删除表记录
    void delete(PrimaryKey key);

    // 返回表记录迭代器
    TableIterator<Record> iterator();

    // 返回符合一定条件的记录，筛选条件通过访问者TableVisitor接口实现
    List<Record> accept(TableVisitor<Record> visitor);
}
