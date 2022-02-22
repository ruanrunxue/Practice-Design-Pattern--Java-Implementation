package com.yrunz.designpattern.monitor;

import com.yrunz.designpattern.monitor.config.PipelineConfig;
import com.yrunz.designpattern.monitor.config.yaml.YamlConfigFactory;
import com.yrunz.designpattern.monitor.pipeline.PipelineFactory;
import com.yrunz.designpattern.monitor.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// 静态内部类机制下的单例模式
public class MonitorSystem {

    private final List<Plugin> installedPlugins;
    private final Path configPath;

    private MonitorSystem(String configPath) {
        this.installedPlugins = new ArrayList<>();
        this.configPath = Paths.get(configPath);
    }

    public static MonitorSystem of(String configPath) {
        return new MonitorSystem(configPath);
    }

    public void start() {
        try {
            Files.list(configPath).forEach(file -> {
                try {
                    String configStr = new String(Files.readAllBytes(file));
                    PipelineConfig config = YamlConfigFactory.newInstance().createPipelineConfig();
                    config.load(configStr);
                    Plugin plugin = PipelineFactory.newInstance().create(config);
                    plugin.install();
                    installedPlugins.add(plugin);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        installedPlugins.forEach(Plugin::uninstall);
        installedPlugins.clear();
    }

}
