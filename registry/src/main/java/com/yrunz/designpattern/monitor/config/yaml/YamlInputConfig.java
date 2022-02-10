package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.config.InputConfig;
import com.yrunz.designpattern.monitor.input.InputType;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class YamlInputConfig extends InputConfig {

    private YamlInputConfig() {
        super();
    }

    public static YamlInputConfig empty() {
        return new YamlInputConfig();
    }

    @Override
    public void load(String conf) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlNode = yaml.load(conf);
        name = (String) yamlNode.get("name");
        type = InputType.valueOf(((String) yamlNode.get("type")).toUpperCase());
        Map<String, Object> context = (Map<String, Object>) yamlNode.get("context");
        for (Map.Entry<String, Object> item : context.entrySet()) {
            ctx.add(item.getKey(), (String) item.getValue());
        }

    }
}
