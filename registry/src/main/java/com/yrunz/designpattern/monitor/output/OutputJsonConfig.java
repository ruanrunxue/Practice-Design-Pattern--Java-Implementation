package com.yrunz.designpattern.monitor.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.exception.LoadConfigException;
import com.yrunz.designpattern.monitor.plugin.Config;

import java.util.Iterator;

public class OutputJsonConfig implements Config {

    private String name;
    private OutputType type;
    private Context ctx;

    private OutputJsonConfig() {
        ctx = Context.empty();
    }

    public static OutputJsonConfig empty() {
        return new OutputJsonConfig();
    }

    @Override
    public void load(String conf) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode config = mapper.readTree(conf);
            name = config.get("name").asText();
            type = OutputType.valueOf(config.get("type").asText().toUpperCase());
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

    public String name() {
        return name;
    }

    public OutputType type() {
        return type;
    }

    public Context context() {
        return ctx;
    }
}
