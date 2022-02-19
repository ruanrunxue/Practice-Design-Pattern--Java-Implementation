package com.yrunz.designpattern.domain;

public class Notification {
    private final String subscriptionId;
    private final Type type;
    // 注册通知时，为新注册的profile；变更通知时，为变更后的profile；去注册通知时，为之前的profile
    private final ServiceProfile profile;

    private Notification(String subscriptionId, Type type, ServiceProfile profile) {
        this.subscriptionId = subscriptionId;
        this.type = type;
        this.profile = profile;
    }

    public static Notification of(String subscriptionId, Type type, ServiceProfile profile) {
        return new Notification(subscriptionId, type, profile);
    }

    public String subscriptionId() {
        return subscriptionId;
    }

    public Type type() {
        return type;
    }

    public ServiceProfile profile() {
        return profile;
    }

    // 通知类型
    public enum Type {
        REGISTER,
        UPDATE,
        DEREGISTER;
    }
}
