package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

// 只具备socket功能的sidecar
public class PureSocketSidecarFactory implements SidecarFactory {

    private PureSocketSidecarFactory() {}

    public static PureSocketSidecarFactory newInstance() {
        return new PureSocketSidecarFactory();
    }

    @Override
    public Socket create() {
        return new SocketImpl();
    }
}
