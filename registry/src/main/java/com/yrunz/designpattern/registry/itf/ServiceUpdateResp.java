package com.yrunz.designpattern.registry.itf;

// 服务更新响应
public class ServiceUpdateResp extends RegistryResp {

    private ServiceUpdateResp(int statusCode, String problemDetails) {
        super(statusCode, problemDetails);
    }

    public static ServiceUpdateResp ok() {
        return new ServiceUpdateResp(200, "");
    }

    public static ServiceUpdateResp error(int statusCode, String problemDetails) {
        return new ServiceUpdateResp(statusCode, problemDetails);
    }

}
