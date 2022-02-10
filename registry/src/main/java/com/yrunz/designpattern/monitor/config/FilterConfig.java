package com.yrunz.designpattern.monitor.config;

import com.yrunz.designpattern.monitor.filter.FilterType;

import java.util.ArrayList;
import java.util.List;

public abstract class FilterConfig implements Config {

    protected List<Item> items;

    protected FilterConfig() {
        items = new ArrayList<>();
    }

    // 子类实现具体加载逻辑
    @Override
    public abstract void load(String conf);

    public List<Item> items() {
        return items;
    }

    public static class Item {
        private String name;
        private FilterType type;

        private Item(String name, FilterType type) {
            this.name = name;
            this.type = type;
        }

        public static Item of(String name, String type) {
            return new Item(name, FilterType.valueOf(type.toUpperCase()));
        }

        public String name() {
            return name;
        }

        public FilterType type() {
            return type;
        }
    }

}
