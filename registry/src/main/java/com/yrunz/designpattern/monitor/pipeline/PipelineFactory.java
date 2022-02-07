package com.yrunz.designpattern.monitor.pipeline;

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
    public Plugin create(Config config) {
        if (!(config instanceof PipelineJsonConfig)) {
            return null;
        }
        PipelineJsonConfig conf = (PipelineJsonConfig) config;
        InputPlugin input = InputPluginFactory.newInstance().create(conf.input());
        FilterPlugin filter = FilterPluginFactory.newInstance().create(conf.filter());
        OutputPlugin output = OutputPluginFactory.newInstance().create(conf.output());
        return Pipeline.of(input, filter, output);
    }
}
