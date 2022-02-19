package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.StatusCode;
import com.yrunz.designpattern.sidecar.SidecarFactory;

// 发货服务
public class ShipmentService extends AbstractService {

    private ShipmentService(String localIp, SidecarFactory sidecarFactory) {
        super(localIp, sidecarFactory, "shipment-service");
    }

    public static ShipmentService of(String localIp, SidecarFactory sidecarFactory) {
        return new ShipmentService(localIp, sidecarFactory);
    }

    @Override
    void startService() {
        httpServer.put("/api/v1/shipment", this::ship);
        httpServer.start();
        System.out.printf("shipment service %s start success.\n", serviceId);
    }

    // 发货成功
    private HttpResp ship(HttpReq req) {
        System.out.printf("shipment service %s ship good success\n", serviceId);
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK);
    }

}
