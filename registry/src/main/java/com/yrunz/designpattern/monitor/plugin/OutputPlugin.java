package com.yrunz.designpattern.monitor.plugin;

public interface OutputPlugin extends Plugin {
    void output(Event event);
    void setContext(Config.Context context);
}
