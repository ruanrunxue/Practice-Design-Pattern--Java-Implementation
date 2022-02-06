package com.yrunz.designpattern.monitor.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.exception.LoadConfigExecption;
import com.yrunz.designpattern.monitor.filter.FilterConfig;
import com.yrunz.designpattern.monitor.input.InputConfig;
import com.yrunz.designpattern.monitor.output.OutputConfig;
import com.yrunz.designpattern.monitor.plugin.Config;

/**
 * pipeline配置定义，格式为json字符串
 * 例子：
 * {"name":"pipline1", "input":{...}, "filter":{...}, "output":{...}}
 */
public class PipelineConfig implements Config {
    private String name;
    private Config input;
    private Config filter;
    private Config output;

    private PipelineConfig() {
        this.input = InputConfig.empty();
        this.filter = FilterConfig.empty();
        this.output = OutputConfig.empty();
    }

    public static PipelineConfig empty() {
        return new PipelineConfig();
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(conf);
            name = config.get("name").asText();
            input.load(config.get("input").asText());
            filter.load(config.get("filter").asText());
            output.load(config.get("output").asText());
        } catch (Exception e) {
            throw new LoadConfigExecption(e.getMessage());
        }
    }

    public String name() {
        return name;
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
