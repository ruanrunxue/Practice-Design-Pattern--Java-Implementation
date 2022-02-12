package com.yrunz.designpattern.registry.itf;

import com.yrunz.designpattern.domain.ServiceProfile;

// 服务注册请求
public class ServiceRegisterReq {
    private final ServiceProfile profile;

    private ServiceRegisterReq(ServiceProfile profile) {
        this.profile = profile;
    }

    public static ServiceRegisterReq of(ServiceProfile profile) {
        return new ServiceRegisterReq(profile);
    }

    public ServiceProfile profile() {
        return profile;
    }
}
