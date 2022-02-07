package com.yrunz.designpattern.monitor.filter;

import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.monitor.plugin.Plugin;

public interface FilterPlugin extends Plugin {
    Event filter(Event event);
}
