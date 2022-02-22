package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.mq.MqProducer;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

// 具备所有功能的sidecar工厂
public class FullFunctionSidecarFactory implements SidecarFactory {

    private MqProducer mqProducer;

    private FullFunctionSidecarFactory() {}

    public static FullFunctionSidecarFactory newInstance() {
        return new FullFunctionSidecarFactory();
    }

    public FullFunctionSidecarFactory withMqProducer(MqProducer mqProducer) {
        this.mqProducer = mqProducer;
        return this;
    }

    @Override
    public Socket create() {
        return new AccessLogSidecar(new FlowCtrlSidecar(new SocketImpl()), mqProducer);
    }

}
