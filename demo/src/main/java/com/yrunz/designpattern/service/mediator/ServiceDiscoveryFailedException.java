package com.yrunz.designpattern.service.mediator;

public class ServiceDiscoveryFailedException extends RuntimeException {
    public ServiceDiscoveryFailedException(String serviceType, String problemDetails) {
        super("discovery " + serviceType + " service failed: " + problemDetails);
    }
}
