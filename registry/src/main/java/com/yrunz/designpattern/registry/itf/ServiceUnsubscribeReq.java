package com.yrunz.designpattern.registry.itf;

// 服务去订阅请求
public class ServiceUnsubscribeReq {
    private final String subscriptionId;

    private ServiceUnsubscribeReq(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public static ServiceUnsubscribeReq of(String subscriptionId) {
        return new ServiceUnsubscribeReq(subscriptionId);
    }

    public String subscriptionId() {
        return subscriptionId;
    }
}
