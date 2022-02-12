package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

import java.util.HashMap;
import java.util.Map;

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

    public void send(SocketData socketData) {
        if (!sockets.containsKey(socketData.dest())) {
            throw new ConnectionRefuseException(socketData.dest());
        }
        sockets.get(socketData.dest()).receive(socketData);
    }
}
