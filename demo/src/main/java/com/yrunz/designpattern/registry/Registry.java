package com.yrunz.designpattern.registry;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.cache.CacheDbProxy;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.db.schema.RegionTable;
import com.yrunz.designpattern.db.schema.ServiceProfileTable;
import com.yrunz.designpattern.db.schema.SubscriptionTable;
import com.yrunz.designpattern.db.transaction.Command;
import com.yrunz.designpattern.db.transaction.InsertCommand;
import com.yrunz.designpattern.db.transaction.Transaction;
import com.yrunz.designpattern.db.transaction.UpdateCommand;
import com.yrunz.designpattern.db.visitor.ServiceProfileVisitor;
import com.yrunz.designpattern.domain.Region;
import com.yrunz.designpattern.domain.ServiceProfile;
import com.yrunz.designpattern.domain.Subscription;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.HttpServer;
import com.yrunz.designpattern.network.http.StatusCode;
import com.yrunz.designpattern.service.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 服务注册中心
public class Registry implements Service {
    private final HttpServer httpServer;
    private final Db db;
    private final String ServiceProfilesTableName;
    private final String RegionsTableName;
    private final String SubscriptionsTableName;

    private Registry(String ip, Socket socket, Db db) {
        this.httpServer = HttpServer.of(socket).listen(ip, 80);
        this.db = CacheDbProxy.of(db);
        this.ServiceProfilesTableName = "service_profiles";
        this.RegionsTableName = "regions";
        this.SubscriptionsTableName = "subscriptions";
    }

    public static Registry of(String ip, Socket socket, Db db) {
        return new Registry(ip, socket, db);
    }

    @Override
    public void run() {
        db.createTableIfNotExist(ServiceProfileTable.of(ServiceProfilesTableName));
        db.createTableIfNotExist(RegionTable.of(RegionsTableName));
        db.createTableIfNotExist(SubscriptionTable.of(SubscriptionsTableName));

        httpServer.put("/api/v1/service-profile", this::register)
                .post("/api/v1/service-profile", this::update)
                .delete("/api/v1/service-profile", this::deregister)
                .get("/api/v1/service-profile", this::discovery)
                .put("/api/v1/subscription", this::subscribe)
                .delete("/api/v1/subscription", this::unsubcribe)
                .start();
    }

    // 服务注册
    private HttpResp register(HttpReq req) {
        if (!(req.body() instanceof ServiceProfile)) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("service register request's body is not ServiceProfile");
        }
        ServiceProfile profile = (ServiceProfile) req.body();
        Transaction transaction = db.createTransaction("register" + profile.id());
        // 事务开始
        transaction.begin();
        // 因为Region表是被关联的，如果Region已经存在了，就没必要在插入一条记录
        Optional<Region> regionRecord = db.query(RegionsTableName, profile.region().id());
        if (!regionRecord.isPresent()) {
            Command insertRegion = InsertCommand.of(RegionsTableName)
                    .withPrimaryKey(profile.region().id())
                    .withRecord(profile.region());
            transaction.exec(insertRegion);
        }
        Command insertProfile = InsertCommand.of(ServiceProfilesTableName)
                .withPrimaryKey(profile.id())
                .withRecord(profile);
        transaction.exec(insertProfile);
        try {
            // 事务提交
            transaction.commit();
        } catch (RecordAlreadyExistException e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails(e.getMessage());
        } catch (Exception e) {
            HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.CREATE);
    }

    // 服务更新
    private HttpResp update(HttpReq req) {
        if (!(req.body() instanceof ServiceProfile)) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("service update request's body is not ServiceProfile");
        }
        ServiceProfile profile = (ServiceProfile) req.body();
        Transaction transaction = db.createTransaction("update" + profile.id());
        // 事务开启
        transaction.begin();
        // 先更新Region表
        Command updateRegion = UpdateCommand.of(RegionsTableName)
                .withPrimaryKey(profile.region().id())
                .withRecord(profile.region());
        transaction.exec(updateRegion);
        // 在更新profile
        Command updateProfile = UpdateCommand.of(ServiceProfilesTableName)
                .withPrimaryKey(profile.id())
                .withRecord(profile);
        transaction.exec(updateProfile);

        try {
            transaction.commit();
        } catch (RecordNotFoundException e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails(e.getMessage());
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK);
    }

    // 服务去注册
    private HttpResp deregister(HttpReq req) {
        if (req.header("serviceId") == null) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("service deregister request not contain serviceId header");
        }
        String serviceId = req.header("serviceId");
        // 去注册只需删除ServiceProfile表记录
        try {
            db.delete(ServiceProfilesTableName, serviceId);
        } catch (RecordNotFoundException e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails(e.getMessage());
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT);
    }

    private HttpResp discovery(HttpReq req) {
        ServiceProfileVisitor visitor = ServiceProfileVisitor.create();
        if (req.queryParam("serviceId") != null) {
            visitor.addServiceId(req.queryParam("serviceId"));
        }
        if (req.queryParam("serviceType") != null) {
            visitor.addServiceType(req.queryParam("serviceType"));
        }
        List<ServiceProfile> result = db.accept(ServiceProfilesTableName, visitor);
        if (result.isEmpty()) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NOT_FOUND);
        }
        // 优先返回优先级高的，如果优先级相等，则返回负载较小的
        ServiceProfile profile = result.stream().sorted((p1, p2) -> {
            if (p1.priority() < p2.priority()) {
                return -1;
            } else if (p1.priority() == p2.priority()) {
                return Integer.compare(p1.load(), p2.load());
            } else {
                return 1;
            }
        }).findAny().get();
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK).addBody(profile);
    }

    private HttpResp subscribe(HttpReq req) {
        if (!(req.body() instanceof Subscription)) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("subscribe request's body is not Subscription");
        }
        Subscription subscription = (Subscription) req.body();
        if (!subscription.id().equals("")) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("subscription id is not empty");
        }
        subscription.withId(UUID.randomUUID().toString());
        try {
            db.insert(SubscriptionsTableName, subscription.id(), subscription);
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId())
                .addStatusCode(StatusCode.CREATE)
                .addHeader("subscriptionId", subscription.id());
    }

    private HttpResp unsubcribe(HttpReq req) {
        if (req.header("subscriptionId") == null) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("unsubcribe request not contain subscriptionId header");
        }
        String subscriptionId = req.header("subscriptionId");
        try {
            db.delete(SubscriptionsTableName, subscriptionId);
        } catch (RecordNotFoundException e) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NOT_FOUND);
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT);
    }
}
