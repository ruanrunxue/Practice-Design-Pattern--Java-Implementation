package com.yrunz.designpattern.registry.itf;

// 服务注册响应
public class ServiceRegisterResp extends RegistryResp{

    private ServiceRegisterResp(int statusCode, String problemDetails) {
        super(statusCode, problemDetails);
    }

    public static ServiceRegisterResp ok() {
        return new ServiceRegisterResp(201, "");
    }

    public static ServiceRegisterResp error(int statusCode, String problemDetails) {
        return new ServiceRegisterResp(statusCode, problemDetails);
    }
}
