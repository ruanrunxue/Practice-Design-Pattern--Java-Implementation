package com.yrunz.designpattern.db.iterator;

import com.yrunz.designpattern.db.TableIterator;

import java.util.Collections;
import java.util.List;

// 随机迭代器
public class RandomIterator<T extends Comparable<T>> implements TableIterator<T> {
    private final List<T> records;
    private int cursor;

    public RandomIterator(List<T> records) {
        Collections.shuffle(records);
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
