package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

/**
 * 观察者模式
 */

public class SocketImpl implements Socket {

    private SocketListener listener;

    @Override
    public void listen(Endpoint endpoint) {
        Network.instance().listen(endpoint, this);
    }

    @Override
    public void close(Endpoint endpoint) {
        Network.instance().disconnect(endpoint);
    }

    @Override
    public void send(SocketData socketData) {
        Network.instance().send(socketData);
    }

    @Override
    public void receive(SocketData socketData) {
        listener.handle(socketData);
    }

    @Override
    public void addListener(SocketListener listener) {
        this.listener = listener;
    }

}
