package com.yrunz.designpattern.service.registry;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.StatusCode;
import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.service.registry.schema.ServiceProfileVisitor;

import java.util.List;

// 服务发现功能
class SvcDiscovery {
    private final Db db;

    SvcDiscovery(Db db) {
        this.db = db;
    }

    // 服务发现
    HttpResp discovery(HttpReq req) {
        ServiceProfileVisitor visitor = ServiceProfileVisitor.create();
        if (req.queryParam("serviceId") != null) {
            visitor.addServiceId(req.queryParam("serviceId"));
        }
        if (req.queryParam("serviceType") != null) {
            visitor.addServiceType(req.queryParam("serviceType"));
        }
        List<ServiceProfile> result = db.accept(RegistryTableName.SERVICE_PROFILES, visitor);
        if (result.isEmpty()) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NOT_FOUND);
        }
        // 优先返回优先级高的，如果优先级相等，则返回负载较小的
        ServiceProfile profile = result.stream().sorted((p1, p2) -> {
            if (p1.priority() < p2.priority()) {
                return -1;
            } else if (p1.priority() == p2.priority()) {
                return Integer.compare(p1.load(), p2.load());
            } else {
                return 1;
            }
        }).findAny().get();
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK).addBody(profile);
    }

}
