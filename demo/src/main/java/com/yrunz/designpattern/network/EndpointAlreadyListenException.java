package com.yrunz.designpattern.network;

public class EndpointAlreadyListenException extends RuntimeException {
    public EndpointAlreadyListenException(String endpoint) {
        super(endpoint + "already listen");
    }
}
