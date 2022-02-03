package db;

import java.util.List;

/**
 * 访问者模式
 *
 */

// 表的访问者接口，用于筛选符合条件的表记录
public interface TableVisitor<Record> {
    // 访问方法，如果table中没有符合条件的记录，则返回empty List
    List<Record> visit(Table<?, Record> table);
}
