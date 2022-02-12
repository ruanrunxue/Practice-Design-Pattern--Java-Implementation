package com.yrunz.designpattern.registry.itf;

// 服务去注册响应
public class ServiceDeregisterResp extends RegistryResp {

    private ServiceDeregisterResp(int statusCode, String problemDetails) {
        super(statusCode, problemDetails);
    }

    public static ServiceDeregisterResp ok() {
        return new ServiceDeregisterResp(204, "");
    }

    public static ServiceDeregisterResp error(int statusCode, String problemDetails) {
        return new ServiceDeregisterResp(statusCode, problemDetails);
    }

}
