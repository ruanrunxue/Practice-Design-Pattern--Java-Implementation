package com.yrunz.designpattern.mq;

// 消息队列接口，继承了Consumable和Producible，同时又consume和produce两种行为
public interface Mq extends Consumable, Producible {
}
