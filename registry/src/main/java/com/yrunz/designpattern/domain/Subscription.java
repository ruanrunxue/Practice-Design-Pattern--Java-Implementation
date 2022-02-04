package com.yrunz.designpattern.domain;

import java.util.UUID;

// 订阅记录对象，记录服务之间的订阅信息，订阅方式支持以下2种：
// 1、按服务Id订阅，ID当对应的服务状态变更时（只能是目标服务只能有1个），通知订阅服务
// 2、按服务类型订阅，当对应类型当服务状态发生变更时（目标服务可以有多个），通知订阅服务
// 3、如果targetServiceId和targetServiceType同时存在时，按照服务ID订阅
// 4、如果targetServiceId和targetServiceType都不存在时，为无效订阅
public class Subscription implements Comparable<Subscription> {
    // 订阅记录ID，全局唯一标识一条订阅记录
    private final String id;
    private String srcServiceId;
    private String targetServiceId;
    private String targetServiceType;

    private Subscription(String id) {
        this.id = id;
        this.srcServiceId = "";
        this.targetServiceId = "";
        this.targetServiceType = "";
    }

    public static Subscription create() {
        return new Subscription(UUID.randomUUID().toString());
    }

    public static Subscription of(String id) {
        return new Subscription(id);
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

    @Override
    public int compareTo(Subscription other) {
        return this.id.compareTo(other.id);
    }
}
