package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.plugin.Plugin;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 桥接模式
 */

// pipeline由input、filter、output三种插件组成，定义了一个数据处理流程
// 数据流向为 input -> filter -> output
public abstract class Pipeline implements Plugin {
    final InputPlugin input;
    final FilterPlugin filter;
    final OutputPlugin output;

    final AtomicBoolean isClose;

    Pipeline(InputPlugin input, FilterPlugin filter, OutputPlugin output) {
        this.input = input;
        this.filter = filter;
        this.output = output;
        this.isClose = new AtomicBoolean(false);
    }

    // 运行pipeline，由子类实现
    abstract void run();

    @Override
    public void install() {
        this.output.install();
        this.filter.install();
        this.input.install();
        this.isClose.set(false);

        run();
    }

    @Override
    public void uninstall() {
        isClose.set(true);
        this.input.uninstall();
        this.filter.uninstall();
        this.output.uninstall();
    }
}
