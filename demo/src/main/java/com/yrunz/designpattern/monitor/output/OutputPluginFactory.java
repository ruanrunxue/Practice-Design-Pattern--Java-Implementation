package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.monitor.config.OutputConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;

public class OutputPluginFactory {

    private OutputPluginFactory() {}

    public static OutputPluginFactory newInstance() {
        return new OutputPluginFactory();
    }

    public OutputPlugin create(OutputConfig config) {
        try {
            Class<?> outputClass = Class.forName(config.type().classPath());
            OutputPlugin output = (OutputPlugin) outputClass.getConstructor().newInstance();
            output.setContext(config.context());
            return output;
        } catch (Exception e) {
            throw new CreatePluginException(config.name(), e.getMessage());
        }
    }
}
