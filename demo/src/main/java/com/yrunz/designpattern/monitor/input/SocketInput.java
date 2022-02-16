package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.network.SocketImpl;
import com.yrunz.designpattern.network.Packet;
import com.yrunz.designpattern.network.SocketListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

// 从网络上获取数据
public class SocketInput implements InputPlugin, SocketListener {

    private final SocketImpl socket;
    private Endpoint endpoint;
    private final Queue<Packet> dataQueue;

    public SocketInput() {
        socket = new SocketImpl();
        dataQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public Event input() {
        Packet data = dataQueue.poll();
        Map<String, String> header = new HashMap<>();
        header.put("peer", data.src().toString());
        return Event.of(header, data.payload());
    }

    @Override
    public void setContext(Config.Context context) {
        String ip = context.getString("ip");
        int port = context.getInt("port");
        this.endpoint = Endpoint.of(ip, port);
    }

    @Override
    public void install() {
        socket.addListener(this);
        socket.listen(endpoint);
    }

    @Override
    public void uninstall() {
        socket.close(endpoint);
    }

    @Override
    public void handle(Packet packet) {
        dataQueue.offer(packet);
    }
}
