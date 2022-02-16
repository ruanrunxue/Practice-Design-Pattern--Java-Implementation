package com.yrunz.designpattern.network;

public class ConnectionRefuseException extends RuntimeException {
    public ConnectionRefuseException(Endpoint endpoint) {
        super(endpoint.toString() + " connection refuse");
    }
}
