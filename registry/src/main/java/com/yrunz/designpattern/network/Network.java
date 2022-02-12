package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

import java.util.HashMap;
import java.util.Map;

public class Network {
    private static final Network INSTANCE = new Network();
    private final Map<Endpoint, Socket> services;

    private Network() {
        services = new HashMap<>();
    }

    public static Network instance() {
        return INSTANCE;
    }

    public void listen(Endpoint endpoint, Socket socket) {
        services.put(endpoint, socket);
    }

    public void send(SocketData socketData) {
        if (!services.containsKey(socketData.dest())) {
            throw new ConnectionRefuseException(socketData.dest());
        }
        services.get(socketData.dest()).receive(socketData);
    }
}
