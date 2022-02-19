package com.yrunz.designpattern.sidecar.flowctrl;

// 流控状态工厂
class FcStateFactory {

    // TPS <= 10：NormalState
    // 10 < TPS <= 50：MinorState
    // TPS > 50: MajorState
    FcState create(int tps) {
        if (tps <= 10) {
            return new NormalState();
        } else if (tps <= 50) {
            return new MinorState();
        } else {
            return new MajorState();
        }
    }
}
