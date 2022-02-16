package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.mq.Message;
import com.yrunz.designpattern.network.Network;
import com.yrunz.designpattern.network.Packet;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketListener;
import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;

// HTTP access log修饰器，拦截socket接收和发送报文，上报access log到MemoryMq上，供监控系统统计分析
public class AccessLogSidecar implements Socket {
    private final Socket socket;
    private final MemoryMq memoryMq;
    private final String topic;

    public AccessLogSidecar(Socket socket) {
        this.socket = socket;
        this.memoryMq = MemoryMq.instance();
        this.topic = "access_log.topic";
    }

    @Override
    public void listen(Endpoint endpoint) {
        Network.instance().listen(endpoint, this);
    }

    @Override
    public void close(Endpoint endpoint) {
        socket.close(endpoint);
    }

    @Override
    public void send(Packet packet) {
        if ((packet.payload() instanceof HttpReq)) {
            String log = String.format("[%s][SEND_REQ]send http request to %s",
                    packet.src(), packet.dest());
            Message message = Message.of(topic, log);
            memoryMq.produce(message);
        }
        if ((packet.payload() instanceof HttpResp)) {
            String log = String.format("[%s][SEND_RESP]send http response to %s",
                    packet.src(), packet.dest());
            Message message = Message.of(topic, log);
            memoryMq.produce(message);
        }

        socket.send(packet);
    }

    @Override
    public void receive(Packet packet) {
        if ((packet.payload() instanceof HttpReq)) {
            String log = String.format("[%s][RECV_REQ]receive http request from %s",
                    packet.dest(), packet.src());
            Message message = Message.of(topic, log);
            memoryMq.produce(message);
        }
        if ((packet.payload() instanceof HttpResp)) {
            String log = String.format("[%s][RECV_RESP]receive http response from %s",
                    packet.dest(), packet.src());
            Message message = Message.of(topic, log);
            memoryMq.produce(message);
        }

        socket.receive(packet);

    }

    @Override
    public void addListener(SocketListener listener) {
        socket.addListener(listener);
    }
}
