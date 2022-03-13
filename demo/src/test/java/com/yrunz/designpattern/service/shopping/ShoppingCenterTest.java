package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.service.registry.model.Region;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.network.Network;
import com.yrunz.designpattern.network.SocketImpl;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.service.mediator.ServiceMediator;
import com.yrunz.designpattern.service.registry.Registry;
import com.yrunz.designpattern.sidecar.RawSocketSidecarFactory;
import com.yrunz.designpattern.sidecar.SidecarFactory;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShoppingCenterTest {

    @After
    public void tearDown() {
        MemoryDb.instance().clear();
        MemoryMq.instance().clear();
        Network.instance().disconnectAll();
    }

    @Test
    public void testShoppingCenter() {
        Db db = MemoryDb.instance();
        SidecarFactory sidecarFactory = RawSocketSidecarFactory.newInstance();
        Region region = Region.of("region-0").withName("Guangdong").withCountry("CHINA");
        Registry registry = Registry.of("192.168.0.1", sidecarFactory, db);
        registry.run();

        ServiceMediator mediator = ServiceMediator.of("192.168.0.2", registry.endpoint(), sidecarFactory);
        mediator.run();

        Service orderService = OrderService.of("192.168.0.3", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        orderService.run();

        Service stockService = StockService.of("192.168.0.4", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        stockService.run();

        Service paymentService = PaymentService.of("192.168.0.5", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        paymentService.run();

        Service shipmentService = ShipmentService.of("192.168.0.6", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        shipmentService.run();

        ShoppingCenter shoppingCenter = ShoppingCenter.of("192.168.0.7", sidecarFactory)
                .withServiceMediator(mediator.endpoint());
        shoppingCenter.run();

        HttpClient client = HttpClient.of(new SocketImpl(), "192.168.0.8");
        HttpReq req = HttpReq.empty()
                .addMethod(HttpMethod.POST)
                .addUri("/shopping-center/api/v1/good")
                .addHeader("user", "paul")
                .addHeader("good", "iphone13");

        HttpResp resp = client.sendReq(shoppingCenter.endpoint(), req);
        assertEquals(StatusCode.OK, resp.statusCode());

    }

}