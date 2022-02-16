package com.yrunz.designpattern.monitor.config;

import com.yrunz.designpattern.monitor.input.InputType;

public abstract class InputConfig implements Config {
    protected String name;
    protected InputType type;
    protected Context ctx;

    protected InputConfig() {
        ctx = Context.empty();
    }

    // 子类实现具体加载逻辑
    @Override
    public abstract void load(String conf);

    public String name() {
        return name;
    }

    public InputType type() {
        return type;
    }

    public Context context() {
        return ctx;
    }

}
