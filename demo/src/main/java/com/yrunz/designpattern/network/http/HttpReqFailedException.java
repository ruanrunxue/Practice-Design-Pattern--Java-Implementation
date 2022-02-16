package com.yrunz.designpattern.network.http;

public class HttpReqFailedException extends RuntimeException {
    public HttpReqFailedException(String message) {
        super(message);
    }
}
