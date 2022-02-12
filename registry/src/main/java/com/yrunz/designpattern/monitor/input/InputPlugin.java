package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.monitor.plugin.Plugin;

/**
 * 策略模式
 */

public interface InputPlugin extends Plugin {
    Event input();
    void setContext(Config.Context context);
}
