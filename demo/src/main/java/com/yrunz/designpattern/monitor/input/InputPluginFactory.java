package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.config.InputConfig;
import com.yrunz.designpattern.monitor.config.json.JsonInputConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.plugin.PluginFactory;

public class InputPluginFactory implements PluginFactory {

    private InputPluginFactory() {}

    public static InputPluginFactory newInstance() {
        return new InputPluginFactory();
    }

    @Override
    public InputPlugin create(Config config) {
        if (!(config instanceof InputConfig)) {
            return null;
        }
        InputConfig conf = (InputConfig) config;
        try {
            Class<?> inputClass = Class.forName(conf.type().classPath());
            InputPlugin input = (InputPlugin) inputClass.getConstructor().newInstance();
            input.setContext(conf.context());
            return input;
        } catch (Exception e) {
            throw new CreatePluginException(conf.name(), e.getMessage());
        }
    }
}