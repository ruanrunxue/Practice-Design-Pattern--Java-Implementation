package com.yrunz.designpattern.network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class Network {

    private static final Network INSTANCE = new Network();
    private final Map<Endpoint, Socket> sockets;

    private Network() {
        sockets = new HashMap<>();
    }

    public static Network instance() {
        return INSTANCE;
    }

    public void listen(Endpoint endpoint, Socket socket) {
        sockets.put(endpoint, socket);
    }

    public void disconnect(Endpoint endpoint) {
        sockets.remove(endpoint);
    }

    public void disconnectAll() {
        sockets.clear();
    }

    public void send(Packet packet) {
        if (!sockets.containsKey(packet.dest())) {
            throw new ConnectionRefuseException(packet.dest());
        }
        Executors.newSingleThreadExecutor().submit(() -> {
            sockets.get(packet.dest()).receive(packet);
        });
    }
}
