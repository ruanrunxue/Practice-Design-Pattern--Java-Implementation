package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.config.PipelineConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.filter.FilterPluginFactory;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.input.InputPluginFactory;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPluginFactory;

public class PipelineFactory {

    private PipelineFactory() {}

    public static PipelineFactory newInstance() {
        return new PipelineFactory();
    }

    public Pipeline create(PipelineConfig config) {
        InputPlugin input = InputPluginFactory.newInstance().create(config.input());
        FilterPlugin filter = FilterPluginFactory.newInstance().create(config.filter());
        OutputPlugin output = OutputPluginFactory.newInstance().create(config.output());
        try {
            Class<?> outputClass = Class.forName(config.type().classPath());
            return (Pipeline) outputClass.getConstructor(InputPlugin.class, FilterPlugin.class, OutputPlugin.class)
                    .newInstance(input, filter, output);
        } catch (Exception e) {
            throw new CreatePluginException(config.name(), e.getMessage());
        }
    }
}
