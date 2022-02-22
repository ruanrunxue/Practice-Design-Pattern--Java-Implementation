package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.monitor.config.InputConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;

public class InputPluginFactory {

    private InputPluginFactory() {}

    public static InputPluginFactory newInstance() {
        return new InputPluginFactory();
    }

    public InputPlugin create(InputConfig config) {
        try {
            Class<?> inputClass = Class.forName(config.type().classPath());
            InputPlugin input = (InputPlugin) inputClass.getConstructor().newInstance();
            input.setContext(config.context());
            return input;
        } catch (Exception e) {
            throw new CreatePluginException(config.name(), e.getMessage());
        }
    }
}
