package com.yrunz.designpattern.sidecar.flowctrl;

import java.util.Random;

// Minor流控状态，随机流控20%的消息
class MinorState extends AbstractFcState {

    @Override
    public boolean tryAccept() {
        Random random = new Random();
        int val = random.nextInt(100);
        return val >= 20;
    }

    @Override
    boolean isSameTo(FcState nextState) {
        return nextState instanceof MinorState;
    }
}
