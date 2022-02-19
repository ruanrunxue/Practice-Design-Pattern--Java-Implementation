package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.StatusCode;
import com.yrunz.designpattern.sidecar.SidecarFactory;

// 支付服务
public class PaymentService extends AbstractService {

    private PaymentService(String localIp, SidecarFactory sidecarFactory) {
        super(localIp, sidecarFactory, "payment-service");
    }

    public static PaymentService of(String localIp, SidecarFactory sidecarFactory) {
        return new PaymentService(localIp, sidecarFactory);
    }

    @Override
    void startService() {
        httpServer.post("/api/v1/payment", this::deduct);
        httpServer.start();
        System.out.printf("payment service %s start success.\n", serviceId);
    }

    // 支付成功
    private HttpResp deduct(HttpReq req) {
        System.out.printf("payment service %s deduct money success\n", serviceId);
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK);
    }

}
