package db.iterator;

import db.TableIterator;

import java.util.*;

// 表记录按顺序迭代，具体排序规则有Record自身实现的Comparable接口定义
public class SortedIterator<T extends Comparable<T>> implements TableIterator<T> {
    private final List<T> records;
    private int cursor;

    public SortedIterator(List<T> records) {
        Collections.sort(records);
        this.records = records;
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < records.size();
    }

    @Override
    public T next() {
        return records.get(cursor++);
    }
}
