package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.monitor.plugin.Plugin;

public interface OutputPlugin extends Plugin {
    void output(Event event);
    void setContext(Config.Context context);
}
