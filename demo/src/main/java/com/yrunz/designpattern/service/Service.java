package com.yrunz.designpattern.service;

import com.yrunz.designpattern.network.Endpoint;

public interface Service {
    // 运行服务
    void run();
    // 返回服务对外提供服务的endpoint
    Endpoint endpoint();
}
