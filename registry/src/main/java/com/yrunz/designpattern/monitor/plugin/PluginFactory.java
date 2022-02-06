package com.yrunz.designpattern.monitor.plugin;

/**
 * 抽象工厂模式
 */

// 抽象插件工厂接口，根据配置实例化插件
public interface PluginFactory {
    Plugin create(Config config);
}
