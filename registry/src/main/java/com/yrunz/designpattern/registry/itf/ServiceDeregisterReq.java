package com.yrunz.designpattern.registry.itf;

// 服务去注册请求
public class ServiceDeregisterReq {
    private final String serviceId;

    private ServiceDeregisterReq(String serviceId) {
        this.serviceId = serviceId;
    }

    public static ServiceDeregisterReq of(String serviceId) {
        return new ServiceDeregisterReq(serviceId);
    }

    public String serviceId() {
        return serviceId;
    }
}
