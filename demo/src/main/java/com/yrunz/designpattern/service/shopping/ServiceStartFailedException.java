package com.yrunz.designpattern.service.shopping;

public class ServiceStartFailedException extends RuntimeException {
    public ServiceStartFailedException(String message) {
        super(message);
    }
}
