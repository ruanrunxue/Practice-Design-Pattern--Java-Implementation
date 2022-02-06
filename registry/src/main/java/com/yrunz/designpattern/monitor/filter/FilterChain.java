package com.yrunz.designpattern.monitor.filter;

import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.monitor.plugin.FilterPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 责任链模式
 */

// 过滤器责任链
public class FilterChain implements FilterPlugin {

    private final List<FilterPlugin> chain;

    private FilterChain() {
        this.chain = new ArrayList<>();
    }

    public static FilterChain create() {
        return new FilterChain();
    }

    @Override
    public Event filter(Event event) {
        for (FilterPlugin filter : chain) {
            event = filter.filter(event);
        }
        return event;
    }

    @Override
    public void install() {

    }

    @Override
    public void uninstall() {

    }

    public FilterChain add(FilterPlugin filter) {
        this.chain.add(filter);
        return this;
    }
}
