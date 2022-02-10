package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.config.json.JsonOutputConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.plugin.*;

public class OutputPluginFactory implements PluginFactory {

    private OutputPluginFactory() {}

    public static OutputPluginFactory newInstance() {
        return new OutputPluginFactory();
    }
    @Override
    public OutputPlugin create(Config config) {
        if (!(config instanceof JsonOutputConfig)) {
            return null;
        }
        JsonOutputConfig conf = (JsonOutputConfig) config;
        try {
            Class<?> outputClass = Class.forName(conf.type().classPath());
            OutputPlugin output = (OutputPlugin) outputClass.getConstructor().newInstance();
            output.setContext(conf.context());
            return output;
        } catch (Exception e) {
            throw new CreatePluginException(conf.name(), e.getMessage());
        }
    }
}
