package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

// 只具备socket功能的sidecar
public class RawSocketSidecarFactory implements SidecarFactory {

    private RawSocketSidecarFactory() {}

    public static RawSocketSidecarFactory newInstance() {
        return new RawSocketSidecarFactory();
    }

    @Override
    public Socket create() {
        return new SocketImpl();
    }
}
