package com.yrunz.designpattern.monitor.config.yaml;

import com.yrunz.designpattern.monitor.config.FilterConfig;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

public class YamlFilterConfig extends FilterConfig {

    private YamlFilterConfig() {
        super();
    }

    public static YamlFilterConfig empty() {
        return new YamlFilterConfig();
    }

    @Override
    public void load(String conf) {
        Yaml yaml = new Yaml();
        List<Object> yamlNodes = yaml.load(conf);
        for (Object node : yamlNodes) {
            Map<String, Object> n = (Map<String, Object>) node;
            FilterConfig.Item item = Item.of((String) n.get("name"), (String) n.get("type"));
            items.add(item);
        }
    }
}
