package com.yrunz.designpattern.registry.itf;

import com.yrunz.designpattern.domain.Subscription;

// 服务订阅请求
public class ServiceSubscribeReq {
    private final Subscription subscription;

    private ServiceSubscribeReq(Subscription subscription) {
        this.subscription = subscription;
    }

    public static ServiceSubscribeReq of(Subscription subscription) {
        return new ServiceSubscribeReq(subscription);
    }

    public Subscription subscription() {
        return subscription;
    }
}
