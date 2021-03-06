package com.yrunz.designpattern.service.registry;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.cache.CacheDbProxy;
import com.yrunz.designpattern.service.registry.model.schema.RegionTable;
import com.yrunz.designpattern.service.registry.model.schema.ServiceProfileTable;
import com.yrunz.designpattern.service.registry.model.schema.SubscriptionTable;
import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.sidecar.SidecarFactory;

/*
 * 单一职责原则（SRP）： 一个模块应该有且只有一个导致其变化的原因
 * SRP是聚合和拆分的一个平衡，太过聚合会导致牵一发动全身，拆分过细又会提升复杂性。
 * 要从用户的视角来把握拆分的度，把面向不同用户的功能拆分开。如果实在无法判断/预测，那就等变化发生时再拆分，避免过度的设计。
 * 例子：
 * Registry将服务管理和服务发现拆分至SvcManagement和SvcDiscovery，符合单一职责原则
 */

// 服务注册中心
public class Registry implements Service {
    private final HttpServer httpServer;
    private final Db db;
    private final String localIp;
    private final SvcManagement svcManagement;
    private final SvcDiscovery svcDiscovery;

    private Registry(String localIp, SidecarFactory sidecarFactory, Db db) {
        this.localIp = localIp;
        this.httpServer = HttpServer.of(sidecarFactory.create()).listen(localIp, 80);
        this.db = CacheDbProxy.of(db);
        this.svcManagement = new SvcManagement(localIp, db, sidecarFactory);
        this.svcDiscovery = new SvcDiscovery(db);
    }

    public static Registry of(String ip, SidecarFactory sidecarFactory, Db db) {
        return new Registry(ip, sidecarFactory, db);
    }

    @Override
    public void run() {
        db.createTableIfNotExist(ServiceProfileTable.of(RegistryTableName.SERVICE_PROFILES));
        db.createTableIfNotExist(RegionTable.of(RegistryTableName.REGIONS));
        db.createTableIfNotExist(SubscriptionTable.of(RegistryTableName.SUBSCRIPTIONS));

        httpServer.put("/api/v1/service-profile", svcManagement::register)
                .post("/api/v1/service-profile", svcManagement::update)
                .delete("/api/v1/service-profile", svcManagement::deregister)
                .get("/api/v1/service-profile", svcDiscovery::discovery)
                .put("/api/v1/subscription", svcManagement::subscribe)
                .delete("/api/v1/subscription", svcManagement::unsubcribe)
                .start();
    }

    @Override
    public Endpoint endpoint() {
        return Endpoint.of(localIp, 80);
    }

}
