package db;

/**
 * 迭代器模式
 *
 */

// 表的迭代器接口
public interface TableIterator<T> {
    // 返回下一个记录，如果hasNext返回false后，再调用则返回null
    T next();
    // 如果迭代完成，返回false，否则返回true
    boolean hasNext();
}
