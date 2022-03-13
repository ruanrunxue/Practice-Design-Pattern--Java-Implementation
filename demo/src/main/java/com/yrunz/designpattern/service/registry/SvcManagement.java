package com.yrunz.designpattern.service.registry;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.db.transaction.Command;
import com.yrunz.designpattern.db.transaction.InsertCommand;
import com.yrunz.designpattern.db.transaction.Transaction;
import com.yrunz.designpattern.db.transaction.UpdateCommand;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.registry.model.Notification;
import com.yrunz.designpattern.service.registry.model.Region;
import com.yrunz.designpattern.service.registry.model.ServiceProfile;
import com.yrunz.designpattern.service.registry.model.Subscription;
import com.yrunz.designpattern.service.registry.model.schema.SubscriptionVisitor;
import com.yrunz.designpattern.sidecar.SidecarFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 服务管理，包含服务注册、更新、去注册。另外，服务订阅、去订阅、通知的功能由于与服务注册、更新、去注册紧密关联，
// 比如，每次的服务通知都是发生在服务状态变更之后，因此也把它们归到服务管理模块。
class SvcManagement {
    private final Db db;
    private final ExecutorService executor;
    private final String localIp;
    private final SidecarFactory sidecarFactory;

    SvcManagement(String localIp, Db db, SidecarFactory sidecarFactory) {
        this.db = db;
        this.localIp = localIp;
        this.sidecarFactory = sidecarFactory;
        this.executor = Executors.newFixedThreadPool(10);
    }

    // 服务注册
    HttpResp register(HttpReq req) {
        if (!(req.body() instanceof ServiceProfile)) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("service register request's body is not ServiceProfile");
        }
        ServiceProfile profile = (ServiceProfile) req.body();
        Transaction transaction = db.createTransaction("register" + profile.id());
        // 事务开始
        transaction.begin();
        // 因为Region表是被关联的，如果Region已经存在了，就没必要在插入一条记录
        Optional<Region> regionRecord = db.query(RegistryTableName.REGIONS, profile.region().id());
        if (!regionRecord.isPresent()) {
            Command insertRegion = InsertCommand.of(RegistryTableName.REGIONS)
                    .withPrimaryKey(profile.region().id())
                    .withRecord(profile.region());
            transaction.exec(insertRegion);
        }
        Command insertProfile = InsertCommand.of(RegistryTableName.SERVICE_PROFILES)
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
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        // 另起线程通知
        executor.submit(() -> notify(Notification.Type.REGISTER, profile));
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.CREATE);
    }

    // 服务更新
    HttpResp update(HttpReq req) {
        if (!(req.body() instanceof ServiceProfile)) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("service update request's body is not ServiceProfile");
        }
        ServiceProfile profile = (ServiceProfile) req.body();
        Transaction transaction = db.createTransaction("update" + profile.id());
        // 事务开启
        transaction.begin();
        // 先更新Region表
        Command updateRegion = UpdateCommand.of(RegistryTableName.REGIONS)
                .withPrimaryKey(profile.region().id())
                .withRecord(profile.region());
        transaction.exec(updateRegion);
        // 在更新profile
        Command updateProfile = UpdateCommand.of(RegistryTableName.SERVICE_PROFILES)
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
        // 另起线程通知
        executor.submit(() -> notify(Notification.Type.UPDATE, profile));
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.OK);
    }

    // 服务去注册
    HttpResp deregister(HttpReq req) {
        if (req.header("serviceId") == null) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("service deregister request not contain serviceId header");
        }
        String serviceId = req.header("serviceId");
        ServiceProfile profile;
        // 去注册只需删除ServiceProfile表记录
        try {
            Optional<ServiceProfile> record = db.query(RegistryTableName.SERVICE_PROFILES, serviceId);
            if (!record.isPresent()) {
                throw new RecordNotFoundException(serviceId);
            }
            profile = record.get();
            db.delete(RegistryTableName.SERVICE_PROFILES, serviceId);
        } catch (RecordNotFoundException e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails(e.getMessage());
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        // 另起线程通知
        executor.submit(() -> notify(Notification.Type.DEREGISTER, profile));
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT);
    }

    // 服务订阅
    HttpResp subscribe(HttpReq req) {
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
            db.insert(RegistryTableName.SUBSCRIPTIONS, subscription.id(), subscription);
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId())
                .addStatusCode(StatusCode.CREATE)
                .addHeader("subscriptionId", subscription.id());
    }

    // 服务去订阅
    HttpResp unsubcribe(HttpReq req) {
        if (req.header("subscriptionId") == null) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.BAD_REQUEST)
                    .addProblemDetails("unsubcribe request not contain subscriptionId header");
        }
        String subscriptionId = req.header("subscriptionId");
        try {
            db.delete(RegistryTableName.SUBSCRIPTIONS, subscriptionId);
        } catch (RecordNotFoundException e) {
            return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NOT_FOUND);
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
        return HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT);
    }

    // 服务通知
    void notify(Notification.Type type, ServiceProfile profile) {
        SubscriptionVisitor visitor = SubscriptionVisitor.create()
                .withTargetServiceId(profile.id())
                .withTargetServiceType(profile.type());
        List<Subscription> subscriptions = db.accept(RegistryTableName.SUBSCRIPTIONS, visitor);
        if (subscriptions.isEmpty()) {
            return;
        }
        HttpClient client = HttpClient.of(sidecarFactory.create(), localIp);
        for (Subscription subscription : subscriptions) {
            Notification notification = Notification.of(subscription.id(), type, profile.clone());
            HttpReq req = HttpReq.empty()
                    .addUri(subscription.notifyUri())
                    .addMethod(HttpMethod.POST)
                    .addBody(notification);
            client.sendReq(subscription.notifyEndpoint(), req);
        }
    }


}
