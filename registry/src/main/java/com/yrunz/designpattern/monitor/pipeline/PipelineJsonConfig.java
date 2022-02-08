package com.yrunz.designpattern.monitor.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.exception.LoadConfigException;
import com.yrunz.designpattern.monitor.filter.FilterJsonConfig;
import com.yrunz.designpattern.monitor.input.InputJsonConfig;
import com.yrunz.designpattern.monitor.output.OutputJsonConfig;
import com.yrunz.designpattern.monitor.plugin.Config;

/**
 * pipeline配置定义，格式为json字符串
 * 例子：
 * {"name":"pipline1", "type":"single_thread", "input":{...}, "filter":{...}, "output":{...}}
 */
public class PipelineJsonConfig implements Config {
    private String name;
    private PipelineType type;
    private Config input;
    private Config filter;
    private Config output;

    private PipelineJsonConfig() {
        this.input = InputJsonConfig.empty();
        this.filter = FilterJsonConfig.empty();
        this.output = OutputJsonConfig.empty();
    }

    public static PipelineJsonConfig empty() {
        return new PipelineJsonConfig();
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(conf);
            name = config.get("name").asText();
            type = PipelineType.valueOf(config.get("type").asText().toUpperCase());
            input.load(config.get("input").toString());
            filter.load(config.get("filter").toString());
            output.load(config.get("output").toString());
        } catch (Exception e) {
            throw new LoadConfigException(e.getMessage());
        }
    }

    public String name() {
        return name;
    }

    public PipelineType type() {
        return type;
    }

    public Config input() {
        return input;
    }

    public Config filter() {
        return filter;
    }

    public Config output() {
        return output;
    }
}
