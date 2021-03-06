package com.yrunz.designpattern.sidecar.flowctrl;

// 正常状态，无须流控
class NormalState extends AbstractFcState {

    @Override
    public boolean tryAccept() {
        return true;
    }

    @Override
    boolean isSameTo(FcState nextState) {
        return nextState instanceof NormalState;
    }

}
