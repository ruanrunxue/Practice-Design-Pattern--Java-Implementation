package com.yrunz.designpattern.db.cmd;

import com.yrunz.designpattern.db.dsl.Result;
import de.vandermeer.asciitable.AsciiTable;

/**
 * 适配器模式
 * 让原本因为接口不匹配而无法一起工作的两个对象能够一起工作
 * 适配器模式所做的就是将一个接口Adaptee，通过适配器Adapter转换成Client所期望的另一个接口Target来使用
 * 在本例中Result为Adaptee，CmdRender为Target，DslResultRender为适配器Adapter
 * 实现适配器模式的几个关键点：
 * 1、定义适配器类Adapter，实现Target接口
 * 2、适配器类Adapter持有Adaptee作为成员属性
 * 3、适配器来的Target接口实现中，将Adaptee输出转换为Target接口输出
 */

// DSL结果Cmd渲染适配器，将DSL输出结果适配为cmd输出
// 关键点1：定义适配器类Adapter，实现Target接口
public class DslResultRender implements CmdRender {
    // 关键点2：适配器类Adapter持有Adaptee作为成员属性
    private final Result result;

    private DslResultRender(Result result) {
        this.result = result;
    }

    public static DslResultRender of(Result result) {
        return new DslResultRender(result);
    }

    // 关键点3：适配器来的Target接口实现中，将Adaptee输出转换为Target接口输出
    @Override
    public String render() {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow(result.toMap().keySet());
        at.addRule();
        at.addRow(result.toMap().values());
        at.addRule();
        return at.render();
    }
}
