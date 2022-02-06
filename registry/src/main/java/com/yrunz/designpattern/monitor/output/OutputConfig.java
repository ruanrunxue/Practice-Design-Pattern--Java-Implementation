package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.monitor.plugin.Config;

public class OutputConfig implements Config {

    private OutputConfig() {}

    public static OutputConfig empty() {
        return new OutputConfig();
    }

    @Override
    public void load(String conf) {

    }
}
