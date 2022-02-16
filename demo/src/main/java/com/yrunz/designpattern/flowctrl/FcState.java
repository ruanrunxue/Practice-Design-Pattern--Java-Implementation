package com.yrunz.designpattern.flowctrl;

/**
 * 状态模式
 */

// 流控状态接口
public interface FcState {
    // 判断当前是否处理请求
    boolean tryAccept();

    // 尝试切换到下一个状态，切换成功返回true，否则false
    boolean trySwitch();

    void setContext(FcContext context);
}
