package com.yrunz.designpattern.monitor.filter;

import com.yrunz.designpattern.monitor.config.FilterConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;

public class FilterPluginFactory {

    private FilterPluginFactory() {
    }

    public static FilterPluginFactory newInstance() {
        return new FilterPluginFactory();
    }

    public FilterPlugin create(FilterConfig config) {
        FilterChain filterChain = FilterChain.empty();
        String name = "";
        try {
            for (FilterConfig.Item item : config.items()) {
                name = item.name();
                Class<?> filterClass = Class.forName(item.type().classPath());
                FilterPlugin filter = (FilterPlugin) filterClass.getConstructor().newInstance();
                filterChain.add(filter);
            }
        } catch (Exception e) {
            throw new CreatePluginException(name, e.getMessage());
        }
        return filterChain;
    }
}
