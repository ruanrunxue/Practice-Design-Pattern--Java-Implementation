package com.yrunz.designpattern.sidecar.flowctrl;

import java.util.Random;

// Major流控状态，随机流控50%的消息
class MajorState extends AbstractFcState {

    @Override
    public boolean tryAccept() {
        Random random = new Random();
        int val = random.nextInt(100);
        return val >= 50;
    }

    @Override
    boolean isSameTo(FcState nextState) {
        return nextState instanceof MajorState;
    }
}
