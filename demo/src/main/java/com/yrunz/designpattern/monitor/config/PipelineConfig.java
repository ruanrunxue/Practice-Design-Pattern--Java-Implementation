package com.yrunz.designpattern.monitor.config;

import com.yrunz.designpattern.monitor.pipeline.PipelineType;

public abstract class PipelineConfig implements Config {
    protected String name;
    protected PipelineType type;
    protected final InputConfig inputConfig;
    protected final FilterConfig filterConfig;
    protected final OutputConfig outputConfig;

    protected PipelineConfig(InputConfig inputConfig, FilterConfig filterConfig, OutputConfig outputConfig) {
        this.inputConfig = inputConfig;
        this.filterConfig = filterConfig;
        this.outputConfig = outputConfig;
    }

    @Override
    public abstract void load(String conf);

    public String name() {
        return name;
    }

    public PipelineType type() {
        return type;
    }

    public InputConfig input() {
        return inputConfig;
    }

    public FilterConfig filter() {
        return filterConfig;
    }

    public OutputConfig output() {
        return outputConfig;
    }

}
