package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.sidecar.SidecarFactory;

/**
 * 外观模式
 */

// 在线商城应用，提供物品购买功能
public class ShoppingCenter implements Service {
    private final HttpServer httpServer;
    private final String localIp;
    private final SidecarFactory sidecarFactory;
    private Endpoint serviceMediatorEndpoint;

    private ShoppingCenter(String localIp, SidecarFactory sidecarFactory) {
        this.localIp = localIp;
        this.sidecarFactory = sidecarFactory;
        this.httpServer = HttpServer.of(sidecarFactory.create()).listen(localIp, 80);
    }

    public static ShoppingCenter of(String localIp, SidecarFactory sidecarFactory) {
        return new ShoppingCenter(localIp, sidecarFactory);
    }

    @Override
    public void run() {
        httpServer.post("/shopping-center/api/v1/good", this::buy).start();
        System.out.println("shopping center start success.");
    }

    @Override
    public Endpoint endpoint() {
        return Endpoint.of(localIp, 80);
    }

    private HttpResp buy(HttpReq req) {
        String good = req.header("good");
        String user = req.header("user");
        System.out.printf("\nuser %s start to buy good %s.\n", user, good);

        HttpClient client = HttpClient.of(sidecarFactory.create(), localIp);

        System.out.println("\nshopping center send create order request to order service.");
        HttpReq orderReq = HttpReq.empty().addUri("/order-service/api/v1/order").addMethod(HttpMethod.PUT);
        HttpResp orderResp = client.sendReq(serviceMediatorEndpoint, orderReq);
        System.out.printf("shopping center receive response from order service, status code %d.\n", orderResp.statusCode().value());

        System.out.println("\nshopping center send check stock request to stock service.");
        HttpReq stockReq = HttpReq.empty().addUri("/stock-service/api/v1/stock").addMethod(HttpMethod.GET);
        HttpResp stockResp = client.sendReq(serviceMediatorEndpoint, stockReq);
        System.out.printf("shopping center receive response from stock service, status code %d.\n", stockResp.statusCode().value());

        System.out.println("\nshopping center send payment request to payment service.");
        HttpReq paymentReq = HttpReq.empty().addUri("/payment-service/api/v1/payment").addMethod(HttpMethod.POST);
        HttpResp paymentResp = client.sendReq(serviceMediatorEndpoint, paymentReq);
        System.out.printf("shopping center receive response from payment service, status code %d.\n", paymentResp.statusCode().value());

        System.out.println("\nshopping center send shipment request to shipment service.");
        HttpReq shipmentReq = HttpReq.empty().addUri("/shipment-service/api/v1/shipment").addMethod(HttpMethod.PUT);
        HttpResp shipmentResp = client.sendReq(serviceMediatorEndpoint, shipmentReq);
        System.out.printf("shopping center receive response from shipment service, status code %d.\n", shipmentResp.statusCode().value());

        System.out.printf("\nuser %s buy good %s success.\n", user, good);
        client.close();
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK);
    }

    public ShoppingCenter withServiceMediator(Endpoint endpoint) {
        this.serviceMediatorEndpoint = endpoint;
        return this;
    }
}
