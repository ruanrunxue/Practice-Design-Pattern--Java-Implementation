package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

/**
 * 观察者模式
 */

public interface Socket {
    void listen(Endpoint endpoint);
    void close(Endpoint endpoint);
    // 监听Socket，入参为Data处理者
    void addListener(SocketListener listener);
    // 发送网络报文
    void send(SocketData socketData);
    // 接收网络报文
    void receive(SocketData socketData);
}
