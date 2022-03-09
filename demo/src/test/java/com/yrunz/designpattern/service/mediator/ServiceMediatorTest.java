package com.yrunz.designpattern.service.mediator;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.service.registry.entity.ServiceStatus;
import com.yrunz.designpattern.network.Network;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.registry.Registry;
import com.yrunz.designpattern.sidecar.RawSocketSidecarFactory;
import com.yrunz.designpattern.sidecar.SidecarFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServiceMediatorTest {

    @After
    public void tearDown() {
        MemoryDb.instance().clear();
        Network.instance().disconnectAll();
    }

    @Test
    public void testForward() {
        SidecarFactory factory = RawSocketSidecarFactory.newInstance();
        Db db = MemoryDb.instance();
        Registry registry = Registry.of("192.168.0.1", factory, db);
        registry.run();

        ServiceMediator mediator = ServiceMediator.of("192.168.0.2", registry.endpoint(), factory);
        mediator.run();

        HttpClient client = HttpClient.of(factory.create(), "192.168.0.3");
        ServiceProfile profile = ServiceProfile.builder("service1")
                .withEndpoint("192.168.0.3", 80)
                .withRegion("0", "region-0", "CHINA")
                .withPriority(1)
                .withStatus(ServiceStatus.NORMAL)
                .withType("order-service")
                .withLoad(100)
                .build();
        HttpReq req = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.PUT)
                .addBody(profile);
        HttpResp resp = client.sendReq(registry.endpoint(), req);
        Assert.assertEquals(StatusCode.CREATE, resp.statusCode());

        HttpServer orderServer = HttpServer.of(factory.create())
                .listen("192.168.0.3", 80)
                .get("/api/v1/order", req1 -> HttpResp.of(req1.reqId()).addStatusCode(StatusCode.OK));
        orderServer.start();

        HttpClient userClient = HttpClient.of(factory.create(), "192.168.0.4");
        HttpReq orderReq = HttpReq.empty()
                .addMethod(HttpMethod.GET)
                .addUri("/order-service/api/v1/order");
        HttpResp orderResp = userClient.sendReq(mediator.endpoint(), orderReq);
        assertEquals(StatusCode.OK, orderResp.statusCode());
    }

}