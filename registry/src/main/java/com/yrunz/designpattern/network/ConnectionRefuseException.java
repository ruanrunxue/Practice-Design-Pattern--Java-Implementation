package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

public class ConnectionRefuseException extends RuntimeException {
    public ConnectionRefuseException(Endpoint endpoint) {
        super(endpoint.toString() + " connection refuse");
    }
}
