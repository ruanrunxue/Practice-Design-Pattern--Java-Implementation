package com.yrunz.designpattern.service.registry;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.service.registry.entity.ServiceStatus;
import com.yrunz.designpattern.service.registry.entity.Subscription;
import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.Network;
import com.yrunz.designpattern.network.SocketImpl;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.sidecar.RawSocketSidecarFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class RegistryTest {

    @After
    public void tearDown() {
        MemoryDb.instance().clear();
        Network.instance().disconnectAll();
    }
    @Test
    public void testRegister() {
        Db db = MemoryDb.instance();
        Registry registry = Registry.of("192.168.0.1", RawSocketSidecarFactory.newInstance(), db);
        registry.run();

        HttpClient client = HttpClient.of(new SocketImpl(), "192.168.0.2");
        ServiceProfile profile = ServiceProfile.builder("service1")
                .withEndpoint("192.168.0.2", 80)
                .withRegion("0", "region-0", "CHINA")
                .withPriority(1)
                .withStatus(ServiceStatus.NORMAL)
                .withType("order")
                .withLoad(100)
                .build();
        HttpReq req = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.PUT)
                .addBody(profile);
        HttpResp resp = client.sendReq(Endpoint.of("192.168.0.1", 80), req);
        Assert.assertEquals(StatusCode.CREATE, resp.statusCode());

        ServiceProfile profile2 = ServiceProfile.builder("service2")
                .withEndpoint("192.168.0.3", 80)
                .withRegion("0", "region-0", "CHINA")
                .withPriority(2)
                .withStatus(ServiceStatus.NORMAL)
                .withType("order")
                .withLoad(100)
                .build();
        HttpReq req2 = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.PUT)
                .addBody(profile2);
        HttpResp resp2 = client.sendReq(Endpoint.of("192.168.0.1", 80), req2);
        Assert.assertEquals(StatusCode.CREATE, resp2.statusCode());

        HttpReq req3 = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.GET)
                .addQueryParam("serviceType", "order");
        HttpResp resp3 = client.sendReq(Endpoint.of("192.168.0.1", 80), req3);
        Assert.assertEquals(StatusCode.OK, resp3.statusCode());
        ServiceProfile serviceProfile = (ServiceProfile) resp3.body();
        Assert.assertEquals("service1", serviceProfile.id());
        Assert.assertEquals(100, serviceProfile.load());

        ServiceProfile profile3 = ServiceProfile.builder("service1")
                .withEndpoint("192.168.0.2", 80)
                .withRegion("0", "region-0", "CHINA")
                .withPriority(1)
                .withStatus(ServiceStatus.NORMAL)
                .withType("order")
                .withLoad(120)
                .build();
        HttpReq req4 = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.POST)
                .addBody(profile3);
        HttpResp resp4 = client.sendReq(Endpoint.of("192.168.0.1", 80), req4);
        Assert.assertEquals(StatusCode.OK, resp4.statusCode());

        HttpReq req5 = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.GET)
                .addQueryParam("serviceId", "service1");
        HttpResp resp5 = client.sendReq(Endpoint.of("192.168.0.1", 80), req5);
        Assert.assertEquals(StatusCode.OK, resp5.statusCode());
        ServiceProfile profile4 = (ServiceProfile) resp5.body();
        Assert.assertEquals("service1", profile4.id());
        Assert.assertEquals(120, profile4.load());

        HttpReq req6 = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.DELETE)
                .addHeader("serviceId", "service1");
        HttpResp resp6 = client.sendReq(Endpoint.of("192.168.0.1", 80), req6);
        Assert.assertEquals(StatusCode.NO_CONTENT, resp6.statusCode());
        HttpReq req7 = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.GET)
                .addQueryParam("serviceId", "service1");
        HttpResp resp7 = client.sendReq(Endpoint.of("192.168.0.1", 80), req7);
        Assert.assertEquals(StatusCode.NOT_FOUND, resp7.statusCode());
    }

    @Test
    public void testSubscribe() {
        Db db = MemoryDb.instance();
        Registry registry = Registry.of("192.168.0.1", RawSocketSidecarFactory.newInstance(), db);
        registry.run();

        HttpClient client = HttpClient.of(new SocketImpl(), "192.168.0.2");
        Subscription subscription =Subscription.create()
                .withSrcServiceId("srcId")
                .withTargetServiceId("targetId")
                .withTargetServiceType("targetType")
                .withNotifyUrl("http://192.168.0.2:80/srcId/notify");
        HttpReq req1 = HttpReq.empty()
                .addUri("/api/v1/subscription")
                .addMethod(HttpMethod.PUT)
                .addBody(subscription);
        HttpResp resp1 = client.sendReq(Endpoint.of("192.168.0.1", 80), req1);
        Assert.assertEquals(StatusCode.CREATE, resp1.statusCode());
        Assert.assertNotNull(resp1.header("subscriptionId"));

        HttpReq req2 = HttpReq.empty()
                .addMethod(HttpMethod.DELETE)
                .addUri("/api/v1/subscription")
                .addHeader("subscriptionId", resp1.header("subscriptionId"));
        HttpResp resp2 = client.sendReq(Endpoint.of("192.168.0.1", 80), req2);
        Assert.assertEquals(StatusCode.NO_CONTENT, resp2.statusCode());
    }

    @Test
    public void testNotify() throws InterruptedException {
        // 启动注册中心
        Registry registry = Registry.of("192.168.0.1", RawSocketSidecarFactory.newInstance(), MemoryDb.instance());
        registry.run();

        // 起通知监听服务器
        AtomicInteger notifyCount = new AtomicInteger();
        HttpServer notifyServer = HttpServer.of(new SocketImpl())
                .listen("192.168.0.2", 80)
                .post("/order/notify", req -> {
                    notifyCount.incrementAndGet();
                    return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT);
                });
        notifyServer.start();

        // 创建订阅
        HttpClient client = HttpClient.of(new SocketImpl(), "192.168.0.2");
        Subscription subscription =Subscription.create()
                .withSrcServiceId("service1")
                .withTargetServiceType("order")
                .withNotifyUrl("http://192.168.0.2:80/order/notify");
        HttpReq req1 = HttpReq.empty()
                .addUri("/api/v1/subscription")
                .addMethod(HttpMethod.PUT)
                .addBody(subscription);
        HttpResp resp1 = client.sendReq(Endpoint.of("192.168.0.1", 80), req1);
        Assert.assertEquals(StatusCode.CREATE, resp1.statusCode());

        // 模拟service2注册
        ServiceProfile profile = ServiceProfile.builder("service2")
                .withEndpoint("192.168.0.3", 80)
                .withRegion("0", "region-0", "CHINA")
                .withPriority(1)
                .withStatus(ServiceStatus.NORMAL)
                .withType("order")
                .withLoad(100)
                .build();
        HttpReq req = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.PUT)
                .addBody(profile);
        HttpResp resp = client.sendReq(Endpoint.of("192.168.0.1", 80), req);
        Assert.assertEquals(StatusCode.CREATE, resp.statusCode());

        Thread.sleep(100);
        // 被正常通知
        Assert.assertEquals(1, notifyCount.get());
    }

}