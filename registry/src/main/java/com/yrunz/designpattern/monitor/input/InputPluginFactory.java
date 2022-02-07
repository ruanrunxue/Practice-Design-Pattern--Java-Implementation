package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.plugin.Config;
import com.yrunz.designpattern.monitor.plugin.PluginFactory;

public class InputPluginFactory implements PluginFactory {

    private InputPluginFactory() {}

    public static InputPluginFactory newInstance() {
        return new InputPluginFactory();
    }

    @Override
    public InputPlugin create(Config config) {
        if (!(config instanceof InputJsonConfig)) {
            return null;
        }
        InputJsonConfig conf = (InputJsonConfig) config;
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
