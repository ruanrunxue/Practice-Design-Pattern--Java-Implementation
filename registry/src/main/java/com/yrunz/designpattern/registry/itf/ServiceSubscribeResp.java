package com.yrunz.designpattern.registry.itf;

// 服务订阅响应
public class ServiceSubscribeResp extends RegistryResp {

    private final String subscriptionId;

    private ServiceSubscribeResp(int statusCode, String problemDetails, String subscriptionId) {
        super(statusCode, problemDetails);
        this.subscriptionId = subscriptionId;
    }

    public static ServiceSubscribeResp ok(String subscriptionId) {
        return new ServiceSubscribeResp(201, "", subscriptionId);
    }

    public static ServiceSubscribeResp error(int statusCode, String problemDetails) {
        return new ServiceSubscribeResp(statusCode, problemDetails, "");
    }

    public String subscriptionId() {
        return subscriptionId;
    }

}
