package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

// 具备所有功能的sidecar工厂
public class FullFunctionSidecarFactory implements SidecarFactory {

    private FullFunctionSidecarFactory() {}

    public static FullFunctionSidecarFactory newInstance() {
        return new FullFunctionSidecarFactory();
    }

    @Override
    public Socket create() {
        return new AccessLogSidecar(new FlowCtrlSidecar(new SocketImpl()));
    }

}
