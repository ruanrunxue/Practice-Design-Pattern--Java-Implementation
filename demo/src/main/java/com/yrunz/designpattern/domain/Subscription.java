package com.yrunz.designpattern.domain;

import com.yrunz.designpattern.network.Endpoint;

// 订阅记录对象，记录服务之间的订阅信息，订阅方式支持以下2种：
// 1、按服务Id订阅，ID当对应的服务状态变更时（只能是目标服务只能有1个），通知订阅服务
// 2、按服务类型订阅，当对应类型当服务状态发生变更时（目标服务可以有多个），通知订阅服务
// 3、如果targetServiceId和targetServiceType同时存在时，按照服务ID订阅
// 4、如果targetServiceId和targetServiceType都不存在时，为无效订阅
public class Subscription implements Comparable<Subscription> {
    // 订阅记录ID，全局唯一标识一条订阅记录，由注册中心产生
    private String id;
    // 订阅方的service id
    private String srcServiceId;
    // 被订阅方的service id和service type
    private String targetServiceId;
    private String targetServiceType;
    // 订阅方接收通知请求的url，形式为http://ip:port/xxx/xxx/xxx
    private String notifyUrl;

    private Subscription() {
        this.id = "";
        this.srcServiceId = "";
        this.targetServiceId = "";
        this.targetServiceType = "";
        this.notifyUrl = "";
    }

    public static Subscription create() {
        return new Subscription();
    }

    public String id() {
        return id;
    }

    public String srcServiceId() {
        return srcServiceId;
    }

    public String targetServiceId() {
        return targetServiceId;
    }

    public String targetServiceType() {
        return targetServiceType;
    }

    public String notifyUrl() {
        return notifyUrl;
    }

    public Subscription withId(String subscriptionId) {
        this.id = subscriptionId;
        return this;
    }

    public Subscription withSrcServiceId(String serviceId) {
        srcServiceId = serviceId;
        return this;
    }

    public Subscription withTargetServiceId(String serviceId) {
        targetServiceId = serviceId;
        return this;
    }

    public Subscription withTargetServiceType(String serviceType) {
        targetServiceType = serviceType;
        return this;
    }

    public Subscription withNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    // 返回被通知方的endpoint
    public Endpoint notifyEndpoint() {
        String url = notifyUrl.replace("http://", "");
        int idx = url.indexOf("/");
        String ipPort = url.substring(0, idx);
        String[] elem = ipPort.split(":");
        return Endpoint.of(elem[0], Integer.parseInt(elem[1]));
    }

    // 返回被通知方的uri，/xxxx/xxxx/xxx
    public String notifyUri() {
        String url = notifyUrl.replace("http://", "");
        int idx = url.indexOf("/");
        return url.substring(idx);
    }

    @Override
    public int compareTo(Subscription other) {
        return this.id.compareTo(other.id);
    }
}
