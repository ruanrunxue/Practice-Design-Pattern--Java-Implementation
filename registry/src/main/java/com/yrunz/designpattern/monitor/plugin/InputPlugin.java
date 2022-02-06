package com.yrunz.designpattern.monitor.plugin;

/**
 * 桥接模式
 */


public interface InputPlugin extends Plugin {
    Event receive();
    void setContext(Config.Context context);
}
