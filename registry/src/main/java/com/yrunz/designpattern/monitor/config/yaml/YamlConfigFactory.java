package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.config.*;

public class YamlConfigFactory implements ConfigFactory {

    private YamlConfigFactory() {}

    public static YamlConfigFactory newInstance() {
        return new YamlConfigFactory();
    }

    @Override
    public InputConfig createInputConfig() {
        return YamlInputConfig.empty();
    }

    @Override
    public FilterConfig createFilterConfig() {
        return YamlFilterConfig.empty();
    }

    @Override
    public OutputConfig createOutputConfig() {
        return YamlOutputConfig.empty();
    }

    @Override
    public PipelineConfig createPipelineConfig() {
        return YamlPipelineConfig.of(createInputConfig(), createFilterConfig(), createOutputConfig());
    }
}
