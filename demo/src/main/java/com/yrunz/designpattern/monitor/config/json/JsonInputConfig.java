package com.yrunz.designpattern.monitor.config.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.config.InputConfig;
import com.yrunz.designpattern.monitor.exception.LoadConfigException;
import com.yrunz.designpattern.monitor.input.InputType;

import java.util.Iterator;

/**
 * input插件配置定义，为json字符串格式，包含name、type、context三个field，
 * 其中context成员也为一个json对象，field和value不固定，根据具体的InputPlugin灵活配置
 * 例子：
 * {"name":"input1", "type":"memory_mq", "context":{"topic":"monitor",...}}
 */
public class JsonInputConfig extends InputConfig {

    private JsonInputConfig() {
        super();
    }

    public static JsonInputConfig empty() {
        return new JsonInputConfig();
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(conf);
            name = config.get("name").asText();
            type = InputType.valueOf(config.get("type").asText().toUpperCase());
            JsonNode ctxNode = config.get("context");
            Iterator<String> fieldNames = ctxNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                ctx.add(fieldName, ctxNode.get(fieldName).asText());
            }
        } catch (Exception e) {
            throw new LoadConfigException(e.getMessage());
        }
    }
}
