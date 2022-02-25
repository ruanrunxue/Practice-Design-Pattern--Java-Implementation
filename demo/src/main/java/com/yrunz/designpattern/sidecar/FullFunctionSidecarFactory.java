package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.mq.Producible;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

// 具备所有功能的sidecar工厂
public class FullFunctionSidecarFactory implements SidecarFactory {

    private Producible producer;

    private FullFunctionSidecarFactory() {}

    public static FullFunctionSidecarFactory newInstance() {
        return new FullFunctionSidecarFactory();
    }

    public FullFunctionSidecarFactory withMqProducer(Producible producible) {
        this.producer = producible;
        return this;
    }

    @Override
    public Socket create() {
        return AccessLogSidecar.wrap(FlowCtrlSidecar.wrap(new SocketImpl()), producer);
    }

}
