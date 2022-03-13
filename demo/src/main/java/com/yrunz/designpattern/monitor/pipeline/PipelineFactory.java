package com.yrunz.designpattern.monitor.pipeline;

import com.yrunz.designpattern.monitor.config.PipelineConfig;
import com.yrunz.designpattern.monitor.exception.CreatePluginException;
import com.yrunz.designpattern.monitor.filter.FilterPlugin;
import com.yrunz.designpattern.monitor.filter.FilterPluginFactory;
import com.yrunz.designpattern.monitor.input.InputPlugin;
import com.yrunz.designpattern.monitor.input.InputPluginFactory;
import com.yrunz.designpattern.monitor.output.OutputPlugin;
import com.yrunz.designpattern.monitor.output.OutputPluginFactory;

/**
 * 里氏替换原则（LSP）：子类型必须能够替换掉它们的基类型，也即基类中的所有性质，在子类中仍能成立。
 * 设计出符合LSP的软件的要点就是，根据该软件的使用者行为作出的合理假设，以此来审视它是否具备有效性和正确性。
 * 要想设计出符合LSP的模型所需要遵循的一些约束：
 * 1、基类应该设计为一个抽象类（不能直接实例化，只能被继承）。
 * 2、子类应该实现基类的抽象接口，而不是重写基类已经实现的具体方法。
 * 3、子类可以新增功能，但不能改变基类的功能。
 * 4、子类不能新增约束，包括抛出基类没有声明的异常。
 * 例子：
 * PipelineFactory中的create方法入参没有使用Config作为入参类型，符合LSP
 */

public class PipelineFactory {

    private PipelineFactory() {}

    public static PipelineFactory newInstance() {
        return new PipelineFactory();
    }

    public Pipeline create(PipelineConfig config) {
        InputPlugin input = InputPluginFactory.newInstance().create(config.input());
        FilterPlugin filter = FilterPluginFactory.newInstance().create(config.filter());
        OutputPlugin output = OutputPluginFactory.newInstance().create(config.output());
        try {
            Class<?> outputClass = Class.forName(config.type().classPath());
            return (Pipeline) outputClass.getConstructor(InputPlugin.class, FilterPlugin.class, OutputPlugin.class)
                    .newInstance(input, filter, output);
        } catch (Exception e) {
            throw new CreatePluginException(config.name(), e.getMessage());
        }
    }
}
