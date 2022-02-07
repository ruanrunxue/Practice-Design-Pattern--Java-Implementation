package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.plugin.Plugin;

import java.util.concurrent.atomic.AtomicBoolean;

// pipeline由input、filter、output三种插件组成，定义了一个数据处理流程
// 数据流向为 input -> filter -> output
public class Pipeline implements Plugin {
    private final InputPlugin input;
    private final FilterPlugin filter;
    private final OutputPlugin output;

    private final AtomicBoolean isClose;

    private Pipeline(InputPlugin input, FilterPlugin filter, OutputPlugin output) {
        this.input = input;
        this.filter = filter;
        this.output = output;
        this.isClose = new AtomicBoolean(false);
    }

    static Pipeline of(InputPlugin input, FilterPlugin filter, OutputPlugin output) {
        return new Pipeline(input, filter, output);
    }

    @Override
    public void install() {
        this.output.install();
        this.filter.install();
        this.input.install();
        this.isClose.set(false);

        while (!isClose.get()) {
            Event event = input.input();
            event = filter.filter(event);
            output.output(event);
        }
    }

    @Override
    public void uninstall() {
        isClose.set(true);
        this.input.uninstall();
        this.filter.uninstall();
        this.output.uninstall();
    }
}
