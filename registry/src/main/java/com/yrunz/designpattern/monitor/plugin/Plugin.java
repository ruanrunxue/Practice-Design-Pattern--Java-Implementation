package com.yrunz.designpattern.monitor.plugin;

// 插件接口，在监控系统中，一切皆为插件
public interface Plugin {
    // 安装插件，只有在安装之后才能运行
    void install();
    // 卸载插件，卸载后停止运行
    void uninstall();
}
