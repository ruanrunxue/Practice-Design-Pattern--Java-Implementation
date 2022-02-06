package com.yrunz.designpattern.monitor.plugin;

public interface FilterPlugin extends Plugin {
    Event filter(Event event);
}
