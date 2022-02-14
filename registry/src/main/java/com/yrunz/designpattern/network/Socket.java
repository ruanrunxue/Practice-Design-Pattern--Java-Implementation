package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

/**
 * 观察者模式
 */

public class Socket {

    private SocketListener listener;

    public void listen(Endpoint endpoint) {
        Network.instance().listen(endpoint, this);
    }

    public void close(Endpoint endpoint) {
        Network.instance().disconnect(endpoint);
    }

    public void send(SocketData socketData) {
        Network.instance().send(socketData);
    }

    public void receive(SocketData socketData) {
        listener.handle(socketData);
    }

    public void addListener(SocketListener listener) {
        this.listener = listener;
    }

}
