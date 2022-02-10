package com.yrunz.designpattern.monitor.config;

import com.yrunz.designpattern.monitor.output.OutputType;

public abstract class OutputConfig implements Config {

    protected String name;
    protected OutputType type;
    protected Context ctx;

    protected OutputConfig() {
        ctx = Context.empty();
    }

    // 子类实现具体加载逻辑
    @Override
    abstract public void load(String conf);

    public String name() {
        return name;
    }

    public OutputType type() {
        return type;
    }

    public Context context() {
        return ctx;
    }

}
