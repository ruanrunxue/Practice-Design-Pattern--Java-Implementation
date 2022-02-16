package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

// 网络通信Socket接口
public interface Socket {
    // 在endpoint指向地址上起监听
    void listen(Endpoint endpoint);
    // 关闭监听
    void close(Endpoint endpoint);
    // 发送网络报文
    void send(Packet packet);
    // 接收网络报文
    void receive(Packet packet);
    // 增加网络报文监听者
    void addListener(SocketListener listener);
}
