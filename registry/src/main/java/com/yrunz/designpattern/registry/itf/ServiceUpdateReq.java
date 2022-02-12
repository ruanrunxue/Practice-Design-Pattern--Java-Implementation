package com.yrunz.designpattern.registry.itf;

import com.yrunz.designpattern.domain.ServiceProfile;

// 服务更新请求
public class ServiceUpdateReq {
    private final ServiceProfile profile;

    private ServiceUpdateReq(ServiceProfile profile) {
        this.profile = profile;
    }

    public static ServiceUpdateReq of(ServiceProfile profile) {
        return new ServiceUpdateReq(profile);
    }

    public ServiceProfile profile() {
        return profile;
    }

}
