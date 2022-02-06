package com.yrunz.designpattern.monitor.filter;

public enum FilterType {
    LOG_TO_JSON("com.yrunz.designpattern.monitor.filter.Log2JsonFilter"),
    ADD_TIMESTAMP("com.yrunz.designpattern.monitor.filter.AddTimestampFilter"),
    JSON_TO_MONITOR_EVENT("com.yrunz.designpattern.monitor.filter.Json2MonitorEventFilter");

    private final String classPath;

    FilterType(String classPath) {
        this.classPath = classPath;
    }

    public String classPath() {
        return classPath;
    }

}
