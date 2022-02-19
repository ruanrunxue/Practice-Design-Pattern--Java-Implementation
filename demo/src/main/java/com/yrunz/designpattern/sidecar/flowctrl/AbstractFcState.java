package com.yrunz.designpattern.sidecar.flowctrl;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 模板方法模式
 */

// 流控状态抽象类
public abstract class AbstractFcState implements FcState {

    private FcContext context;

    // 模板方法，尝试切换到下个状态
    @Override
    public boolean trySwitch() {
        long nowTimestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        long interval = nowTimestamp - context.lastTimestamp.get();
        if (interval < 1) {
            return false;
        }
        context.lastTimestamp.set(nowTimestamp);
        int tps = context.reqCount.getAndSet(0) / (int) interval;
        FcState nextState = context.factory.create(tps);
        if (isSameTo(nextState)) {
            return false;
        }
        context.switchTo(nextState);
        return true;
    }

    @Override
    public void setContext(FcContext context) {
        this.context = context;
    }

    // 子类实现, 判断当前状态是否于下一个状态一致，如果一致，则无须切换
    abstract boolean isSameTo(FcState nextState);

}
