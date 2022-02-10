package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.config.FilterConfig;
import com.yrunz.designpattern.monitor.config.InputConfig;
import com.yrunz.designpattern.monitor.config.OutputConfig;
import com.yrunz.designpattern.monitor.config.PipelineConfig;
import com.yrunz.designpattern.monitor.pipeline.PipelineType;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class YamlPipelineConfig extends PipelineConfig {

    private YamlPipelineConfig(InputConfig inputConfig, FilterConfig filterConfig, OutputConfig outputConfig) {
        super(inputConfig, filterConfig, outputConfig);
    }

    public static YamlPipelineConfig of(InputConfig inputConfig, FilterConfig filterConfig, OutputConfig outputConfig) {
        return new YamlPipelineConfig(inputConfig, filterConfig, outputConfig);
    }

    @Override
    public void load(String conf) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlNode = yaml.load(conf);
        name = (String) yamlNode.get("name");
        type = PipelineType.valueOf(((String) yamlNode.get("type")).toUpperCase());
        Object inputNode = yamlNode.get("input");
        inputConfig.load(yaml.dump(inputNode));
        Object filterNode = yamlNode.get("filter");
        filterConfig.load(yaml.dump(filterNode));
        Object outputNode = yamlNode.get("output");
        outputConfig.load(yaml.dump(outputNode));
    }
}
