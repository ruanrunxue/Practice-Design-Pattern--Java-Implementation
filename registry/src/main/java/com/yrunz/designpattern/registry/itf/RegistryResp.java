package com.yrunz.designpattern.registry.itf;

// 注册中心响应基类
public abstract class RegistryResp {
    private final int statusCode;
    private final String problemDetails;

    RegistryResp(int statusCode, String problemDetails) {
        this.statusCode = statusCode;
        this.problemDetails = problemDetails;
    }

    public int statusCode() {
        return statusCode;
    }

    public String details() {
        return problemDetails;
    }
}
