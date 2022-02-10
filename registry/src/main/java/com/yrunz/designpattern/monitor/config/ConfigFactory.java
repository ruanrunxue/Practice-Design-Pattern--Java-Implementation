package com.yrunz.designpattern.monitor.config;

/**
 * 抽象工厂模式
 */

// 配置抽象工厂接口
public interface ConfigFactory {
    InputConfig createInputConfig();
    FilterConfig createFilterConfig();
    OutputConfig createOutputConfig();
    PipelineConfig createPipelineConfig();
}
