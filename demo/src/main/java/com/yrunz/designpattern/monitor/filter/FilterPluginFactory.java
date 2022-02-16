package com.yrunz.designpattern.monitor.filter;

import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.config.FilterConfig;
import com.yrunz.designpattern.monitor.config.json.JsonFilterConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.plugin.*;

public class FilterPluginFactory implements PluginFactory {

    private FilterPluginFactory() {
    }

    public static FilterPluginFactory newInstance() {
        return new FilterPluginFactory();
    }

    @Override
    public FilterPlugin create(Config config) {
        if (!(config instanceof FilterConfig)) {
            return null;
        }
        FilterConfig conf = (FilterConfig) config;
        FilterChain filterChain = FilterChain.empty();
        String name = "";
        try {
            for (FilterConfig.Item item : conf.items()) {
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
