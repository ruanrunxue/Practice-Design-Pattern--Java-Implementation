package com.yrunz.designpattern.monitor.model;


import java.util.concurrent.atomic.AtomicInteger;

// 监控事件
public class MonitorEvent implements Comparable<MonitorEvent> {
    // 事件ID生成器
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final String id;
    private final String serviceId;
    private final Type eventType;
    private final long timestamp;

    private MonitorEvent(String id, String serviceId, Type eventType, long timestamp) {
        this.id = id;
        this.serviceId = serviceId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    // ID自动生成
    public static MonitorEvent of(String serviceId, Type eventType, long timestamp) {
        String id = Integer.toString(ID_GENERATOR.getAndIncrement());
        return new MonitorEvent(id, serviceId, eventType, timestamp);
    }

    public static MonitorEvent of(String id, String serviceId, Type eventType, long timestamp) {
        return new MonitorEvent(id, serviceId, eventType, timestamp);
    }

    public String id() {
        return id;
    }

    public String serviceId() {
        return serviceId;
    }

    public Type eventType() {
        return eventType;
    }

    public long timestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(MonitorEvent other) {
        return this.id.compareTo(other.id);
    }

    // 监控事件类型，当前有4种：RECV_REQ 接收请求，RECV_RESP 接收响应，SEND_REQ 发送请求，SEND_RESP 发送响应
    public enum Type {
        RECV_REQ,
        RECV_RESP,
        SEND_REQ,
        SEND_RESP
    }
}
