package com.yrunz.designpattern.monitor.input;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.exception.LoadConfigExecption;
import com.yrunz.designpattern.monitor.plugin.Config;

/**
 * input插件配置定义，为json字符串格式，包含name、type、context三个field，
 * 其中context成员也为一个json对象，field和value不固定，根据具体的InputPlugin灵活配置
 * 例子：
 * {"name":"input1", "type":"memory_mq", "context":{"topic":"monitor",...}}
 */
public class InputConfig implements Config {

    private String name;
    private InputType type;
    private Context ctx;

    private InputConfig() {
        ctx = Context.empty();
    }

    public static InputConfig empty() {
        return new InputConfig();
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(conf);
            name = config.get("name").asText();
            type = InputType.valueOf(config.get("type").asText().toUpperCase());
            JsonNode ctxNode = config.get("context");
            while (ctxNode.fieldNames().hasNext()) {
                String fieldName = ctxNode.fieldNames().next();
                ctx.add(fieldName, ctxNode.get(fieldName).asText());
            }
        } catch (Exception e) {
            throw new LoadConfigExecption(e.getMessage());
        }
    }

    public String name() {
        return name;
    }

    public InputType type() {
        return type;
    }

    public Context context() {
        return ctx;
    }
}
