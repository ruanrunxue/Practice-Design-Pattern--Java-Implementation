package com.yrunz.designpattern.service.registry;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.cache.CacheDbProxy;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.service.registry.schema.RegionTable;
import com.yrunz.designpattern.service.registry.schema.ServiceProfileTable;
import com.yrunz.designpattern.service.registry.schema.SubscriptionTable;
import com.yrunz.designpattern.db.transaction.Command;
import com.yrunz.designpattern.db.transaction.InsertCommand;
import com.yrunz.designpattern.db.transaction.Transaction;
import com.yrunz.designpattern.db.transaction.UpdateCommand;
import com.yrunz.designpattern.service.registry.schema.ServiceProfileVisitor;
import com.yrunz.designpattern.service.registry.schema.SubscriptionVisitor;
import com.yrunz.designpattern.service.registry.entity.Notification;
import com.yrunz.designpattern.service.registry.entity.Region;
import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.service.registry.entity.Subscription;
import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.sidecar.SidecarFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
