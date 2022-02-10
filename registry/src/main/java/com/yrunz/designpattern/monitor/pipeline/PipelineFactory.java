package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.config.json.JsonPipelineConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.filter.FilterPluginFactory;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.input.InputPluginFactory;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPluginFactory;
import com.yrunz.designpattern.monitor.plugin.*;

public class PipelineFactory implements PluginFactory {

    private PipelineFactory() {}

    public static PipelineFactory newInstance() {
        return new PipelineFactory();
    }

    @Override
    public Pipeline create(Config config) {
        if (!(config instanceof JsonPipelineConfig)) {
            return null;
        }
        JsonPipelineConfig conf = (JsonPipelineConfig) config;
        InputPlugin input = InputPluginFactory.newInstance().create(conf.input());
        FilterPlugin filter = FilterPluginFactory.newInstance().create(conf.filter());
        OutputPlugin output = OutputPluginFactory.newInstance().create(conf.output());
        try {
            Class<?> outputClass = Class.forName(conf.type().classPath());
            return (Pipeline) outputClass.getConstructor(InputPlugin.class, FilterPlugin.class, OutputPlugin.class)
                    .newInstance(input, filter, output);
        } catch (Exception e) {
            throw new CreatePluginException(conf.name(), e.getMessage());
        }
    }
}
