package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.mq.Producible;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

// 具备所有功能的sidecar工厂
public class AllInOneSidecarFactory implements SidecarFactory {

    private Producible producer;

    private AllInOneSidecarFactory() {}

    public static AllInOneSidecarFactory newInstance() {
        return new AllInOneSidecarFactory();
    }

    public AllInOneSidecarFactory withMqProducer(Producible producible) {
        this.producer = producible;
        return this;
    }

    @Override
    public Socket create() {
        return AccessLogSidecar.wrap(FlowCtrlSidecar.wrap(new SocketImpl()), producer);
    }

}
