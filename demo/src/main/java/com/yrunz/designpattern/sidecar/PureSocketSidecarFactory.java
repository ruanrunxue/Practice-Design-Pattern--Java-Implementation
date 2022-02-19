package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;

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
