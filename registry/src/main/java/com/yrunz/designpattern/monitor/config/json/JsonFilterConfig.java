package com.yrunz.designpattern.monitor.config.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.config.FilterConfig;
import com.yrunz.designpattern.monitor.exception.LoadConfigException;

/**
 * filter插件配置定义，为json字符串格式，是一个数组，元素包含name、type两个个field，
 * 例子：
 * [{"name":"filter1", "type":"to_json"},{"name":"filter2", "type":"add_timestamp"},...]
 */
public class JsonFilterConfig extends FilterConfig {

    private JsonFilterConfig() {
        super();
    }

    public static JsonFilterConfig empty() {
        return new JsonFilterConfig();
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode filterNodes = mapper.readTree(conf);
            if (!filterNodes.isArray()) {
                throw new LoadConfigException("filter config is not json array");
            }
            for (JsonNode node : filterNodes) {
                FilterConfig.Item item = FilterConfig.Item.of(node.get("name").asText(), node.get("type").asText());
                items.add(item);
            }
        } catch (Exception e) {
            throw new LoadConfigException(e.getMessage());
        }
    }

}
