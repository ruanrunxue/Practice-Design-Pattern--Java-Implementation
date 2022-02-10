package com.yrunz.designpattern.monitor.config.json;

import com.yrunz.designpattern.monitor.config.*;

public class JsonConfigFactory implements ConfigFactory {

    private JsonConfigFactory() {}

    public static JsonConfigFactory newInstance() {
        return new JsonConfigFactory();
    }

    @Override
    public InputConfig createInputConfig() {
        return JsonInputConfig.empty();
    }

    @Override
    public FilterConfig createFilterConfig() {
        return JsonFilterConfig.empty();
    }

    @Override
    public OutputConfig createOutputConfig() {
        return JsonOutputConfig.empty();
    }

    @Override
    public PipelineConfig createPipelineConfig() {
        return JsonPipelineConfig.of(createInputConfig(), createFilterConfig(), createOutputConfig());
    }
}
