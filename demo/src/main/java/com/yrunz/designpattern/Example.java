package com.yrunz.designpattern;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.service.registry.model.Region;
import com.yrunz.designpattern.monitor.MonitorSystem;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.service.mediator.ServiceMediator;
import com.yrunz.designpattern.service.registry.Registry;
import com.yrunz.designpattern.service.shopping.*;
import com.yrunz.designpattern.sidecar.AllInOneSidecarFactory;
import com.yrunz.designpattern.sidecar.SidecarFactory;

public class Example {

    public static void main(String[] args) {
        Db db = MemoryDb.instance();
        SidecarFactory sidecarFactory = AllInOneSidecarFactory.newInstance().withMqProducer(MemoryMq.instance());
        Region region = Region.of("region-0").withName("Guangdong").withCountry("CHINA");

        // 启动监控系统
        MonitorSystem.of("src/main/resources/pipelines").start();

        // 启动注册中心
        Registry registry = Registry.of("192.168.0.1", sidecarFactory, db);
        registry.run();

        // 启动服务中介
        ServiceMediator mediator = ServiceMediator.of("192.168.0.2", registry.endpoint(), sidecarFactory);
        mediator.run();

        // 启动订单服务
        Service orderService = OrderService.of("192.168.0.3", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        orderService.run();

        // 启动库存服务
        Service stockService = StockService.of("192.168.0.4", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        stockService.run();

        // 启动支付服务
        Service paymentService = PaymentService.of("192.168.0.5", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        paymentService.run();

        // 启动发货服务
        Service shipmentService = ShipmentService.of("192.168.0.6", sidecarFactory)
                .atRegion(region)
                .withRegistryEndpoint(registry.endpoint())
                .withPriority(0)
                .withLoad(100);
        shipmentService.run();

        // 启动在线商城
        ShoppingCenter shoppingCenter = ShoppingCenter.of("192.168.0.7", sidecarFactory)
                .withServiceMediator(mediator.endpoint());
        shoppingCenter.run();

        // 消费者从在线商城上购买商品
        Consumer.name("paul")
                .useMobilePhone("192.168.0.8")
                .loginShoppingCenter(shoppingCenter.endpoint())
                .buyGood("iphone13");

        System.exit(0);
    }
}
