package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.StatusCode;
import com.yrunz.designpattern.sidecar.SidecarFactory;

// 库存服务
public class StockService extends AbstractService {

    private StockService(String localIp, SidecarFactory sidecarFactory) {
        super(localIp, sidecarFactory, "stock-service");
    }

    public static StockService of(String localIp, SidecarFactory sidecarFactory) {
        return new StockService(localIp, sidecarFactory);
    }

    @Override
    void startService() {
        httpServer.get("/api/v1/stock", this::check);
        httpServer.start();
        System.out.printf("stock service %s start success.\n", serviceId);
    }

    // 检查库存成功
    private HttpResp check(HttpReq req) {
        System.out.printf("stock service %s check stock success\n", serviceId);
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK);
    }

}
