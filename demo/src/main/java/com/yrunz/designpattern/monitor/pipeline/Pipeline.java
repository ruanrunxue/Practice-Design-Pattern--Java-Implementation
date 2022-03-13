package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.plugin.Plugin;

import java.util.concurrent.atomic.AtomicBoolean;

/*
 * 开闭原则（OCP）：一个软件系统应该具备良好的可扩展性，新增功能应当通过扩展的方式实现，而不是在已有的代码基础上修改
 * 根据具体的业务场景识别出那些最有可能变化的点，然后分离出去，抽象成稳定的接口。
 * 后续新增功能时，通过扩展接口，而不是修改已有代码实现
 * 例子：
 * Pipeline将输入、过滤、输出三个独立变化点，分离到三个接口InputPlugin、FilterPlugin、OutputPlugin上，符合OCP
 */

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
