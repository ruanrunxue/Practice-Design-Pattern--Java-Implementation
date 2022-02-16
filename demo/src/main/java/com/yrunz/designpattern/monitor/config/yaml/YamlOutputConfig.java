package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.config.OutputConfig;
import com.yrunz.designpattern.monitor.output.OutputType;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * output插件配置定义，为yaml字符串格式，包含name、type、context三个field，
 * 例子：
 * name: "output1"
 * type: "memory_db"
 * context:
 *   - tableName: "test"
 */
public class YamlOutputConfig extends OutputConfig {

    private YamlOutputConfig() {
        super();
    }

    public static YamlOutputConfig empty() {
        return new YamlOutputConfig();
    }

    @Override
    public void load(String conf) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlNode = yaml.load(conf);
        name = (String) yamlNode.get("name");
        type = OutputType.valueOf(((String) yamlNode.get("type")).toUpperCase());
        Map<String, Object> context = (Map<String, Object>) yamlNode.get("context");
        for (Map.Entry<String, Object> item : context.entrySet()) {
            ctx.add(item.getKey(), (String) item.getValue());
        }
    }
}
