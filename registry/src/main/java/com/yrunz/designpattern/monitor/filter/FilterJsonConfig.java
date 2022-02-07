package com.yrunz.designpattern.monitor.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yrunz.designpattern.monitor.exception.LoadConfigException;
import com.yrunz.designpattern.monitor.plugin.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * filter插件配置定义，为json字符串格式，是一个数组，元素包含name、type两个个field，
 * 例子：
 * [{"name":"filter1", "type":"to_json"},{"name":"filter2", "type":"add_timestamp"},...]
 */
public class FilterJsonConfig implements Config {

    private List<Item> items;

    private FilterJsonConfig() {
        items = new ArrayList<>();
    }

    public static FilterJsonConfig empty() {
        return new FilterJsonConfig();
    }

    public List<Item> items() {
        return items;
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
                Item item = Item.of(node.get("name").asText(), node.get("type").asText());
                items.add(item);
            }
        } catch (Exception e) {
            throw new LoadConfigException(e.getMessage());
        }
    }

    public static class Item {
        private String name;
        private FilterType type;

        private Item(String name, FilterType type) {
            this.name = name;
            this.type = type;
        }

        public static Item of(String name, String type) {
            return new Item(name, FilterType.valueOf(type.toUpperCase()));
        }

        public String name() {
            return name;
        }

        public FilterType type() {
            return type;
        }
    }
}
