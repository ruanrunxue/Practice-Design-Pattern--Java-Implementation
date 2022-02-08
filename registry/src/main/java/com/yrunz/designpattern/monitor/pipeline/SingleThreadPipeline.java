package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.plugin.Event;

import java.util.concurrent.Executors;

// 每个Pipeline都独立起一个线程运行
public class SingleThreadPipeline extends Pipeline {

    public SingleThreadPipeline(InputPlugin input, FilterPlugin filter, OutputPlugin output) {
        super(input, filter, output);
    }

    @Override
    public void run() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (!isClose.get()) {
                Event event = input.input();
                event = filter.filter(event);
                output.output(event);
            }
        });
    }
}
