package com.yrunz.designpattern.monitor.config.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.config.FilterConfig;
import com.yrunz.designpattern.monitor.config.InputConfig;
import com.yrunz.designpattern.monitor.config.OutputConfig;
import com.yrunz.designpattern.monitor.config.PipelineConfig;
import com.yrunz.designpattern.monitor.exception.LoadConfigException;
import com.yrunz.designpattern.monitor.pipeline.PipelineType;

/**
 * pipeline配置定义，格式为json字符串
 * 例子：
 * {"name":"pipline1", "type":"single_thread", "input":{...}, "filter":{...}, "output":{...}}
 */
public class JsonPipelineConfig extends PipelineConfig {

    private JsonPipelineConfig(InputConfig inputConfig, FilterConfig filterConfig, OutputConfig outputConfig) {
        super(inputConfig, filterConfig, outputConfig);
    }

    public static JsonPipelineConfig of(InputConfig inputConfig, FilterConfig filterConfig, OutputConfig outputConfig) {
        return new JsonPipelineConfig(inputConfig, filterConfig, outputConfig);
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(conf);
            name = config.get("name").asText();
            type = PipelineType.valueOf(config.get("type").asText().toUpperCase());
            inputConfig.load(config.get("input").toString());
            filterConfig.load(config.get("filter").toString());
            outputConfig.load(config.get("output").toString());
        } catch (Exception e) {
            throw new LoadConfigException(e.getMessage());
        }
    }
}
