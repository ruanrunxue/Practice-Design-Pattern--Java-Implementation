package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.StatusCode;
import com.yrunz.designpattern.sidecar.SidecarFactory;

// 订单服务
public class OrderService extends AbstractService {

    private OrderService(String localIp, SidecarFactory sidecarFactory) {
        super(localIp, sidecarFactory, "order-service");
    }

    public static OrderService of(String localIp, SidecarFactory sidecarFactory) {
        return new OrderService(localIp, sidecarFactory);
    }

    @Override
    void startService() {
        httpServer.put("/api/v1/order", this::createOrder);
        httpServer.start();
        System.out.printf("order service %s start success.\n", serviceId);
    }

    // 创建订单成功
    private HttpResp createOrder(HttpReq req) {
        System.out.printf("order service %s create order success\n", serviceId);
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.CREATE);
    }
}
