package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.plugin.Config;
import com.yrunz.designpattern.monitor.plugin.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 通过线程池来运行Pipeline
public class ThreadPoolPipeline extends Pipeline {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public ThreadPoolPipeline(InputPlugin input, FilterPlugin filter, OutputPlugin output) {
        super(input, filter, output);
    }

    @Override
    public void run() {
        threadPool.submit(() -> {
            while (!isClose.get()) {
                Event event = input.input();
                event = filter.filter(event);
                output.output(event);
            }
        });
    }
}
