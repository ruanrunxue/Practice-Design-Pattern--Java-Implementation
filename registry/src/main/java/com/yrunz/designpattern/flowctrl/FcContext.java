package com.yrunz.designpattern.flowctrl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

// 流控状态上下文，根据每秒处理请求速率进行流控
public class FcContext {
    final AtomicInteger reqCount;
    // 上一次更新的时间戳，每秒更新一次
    final AtomicLong lastTimestamp;
    FcState curState;
    FcStateFactory factory;

    private FcContext() {
        this.reqCount = new AtomicInteger(0);
        long nowTimestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.lastTimestamp = new AtomicLong(nowTimestamp);
        this.factory = new FcStateFactory();
    }

    public static FcContext create() {
        FcContext context = new FcContext();
        context.switchTo(new NormalState());
        return context;
    }

    void switchTo(FcState newState) {
        newState.setContext(this);
        this.curState = newState;
    }

    // 判断是否应该接收请求
    public boolean tryAccept() {
        reqCount.incrementAndGet();
        curState.trySwitch();
        return curState.tryAccept();
    }


}
