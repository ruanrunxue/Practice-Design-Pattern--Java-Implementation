package com.yrunz.designpattern.monitor.plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * 组合模式
 */

public interface Config {
    // 从json字符串中加载配置
    void load(String conf);

    class Context {
        private final Map<String, String> ctxs;

        private Context() {
            ctxs = new HashMap<>();
        }

        public static Context empty() {
            return new Context();
        }

        public void add(String key, String value) {
            ctxs.put(key, value);
        }

        public String getString(String key) {
            return ctxs.get(key);
        }

        public int getInt(String key) {
            return Integer.parseInt(ctxs.get(key));
        }
    }

}
