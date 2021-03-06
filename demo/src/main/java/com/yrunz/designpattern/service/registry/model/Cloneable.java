package com.yrunz.designpattern.service.registry.model;

/**
 * 原型模式
 * 主要解决对象复制的问题，它的核心就是`clone()`方法，返回`Prototype`对象的复制品
 * 适用于需要多个相同对象副本的场景，可避免将对象内部细节暴露给使用者
 * 实现原型模式的几个关键点：
 * 1、定义原型接口，其中带有clone方法，其中返回值为原型对象
 * 2、需要复制的对象实现原型接口，并在clone方法中实现对象复制逻辑
 */

// 对象复制接口，当对象有复制需求时，可实现该接口，在clone方法中实现对象复制逻辑
public interface Cloneable<T> {
    // 原型模式 关键点1：定义对象复制方法，返回原型对象
    T clone();
}
