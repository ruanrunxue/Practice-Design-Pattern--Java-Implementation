package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.plugin.Config;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.monitor.plugin.Plugin;

/**
 * 桥接模式
 */


public interface InputPlugin extends Plugin {
    Event input();
    void setContext(Config.Context context);
}
