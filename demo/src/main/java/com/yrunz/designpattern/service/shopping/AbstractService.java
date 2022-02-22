package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.service.registry.entity.Region;
import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.service.registry.entity.ServiceStatus;
import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.sidecar.SidecarFactory;

import java.util.UUID;

// 商城应用服务基类，启动时向注册中心注册
public abstract class AbstractService implements Service {
    final HttpServer httpServer;
    final String localIp;
    final SidecarFactory sidecarFactory;
    final String serviceId;
    final String serviceType;
    Region region;
    int priority;
    int load;
    Endpoint registryEndpoint;

    AbstractService(String localIp, SidecarFactory sidecarFactory, String serviceType) {
        this.localIp = localIp;
        this.sidecarFactory = sidecarFactory;
        this.httpServer = HttpServer.of(sidecarFactory.create()).listen(localIp, 80);
        this.serviceId = UUID.randomUUID().toString();
        this.serviceType = serviceType;
    }

    @Override
    public void run() {
        HttpClient client = HttpClient.of(sidecarFactory.create(), localIp);
        ServiceProfile profile = ServiceProfile.builder(serviceId)
                .withType(serviceType)
                .withRegion(region.id(), region.name(), region.country())
                .withEndpoint(localIp, 80)
                .withStatus(ServiceStatus.NORMAL)
                .withPriority(0)
                .withLoad(100)
                .build();
        HttpReq req = HttpReq.empty()
                .addMethod(HttpMethod.PUT)
                .addUri("/api/v1/service-profile")
                .addBody(profile);
        HttpResp resp = client.sendReq(registryEndpoint, req);
        if (!resp.isSuccess()) {
            throw new ServiceStartFailedException("register to Registry failed: " + resp.problemDetails());
        }
        // 注册成功后对外提供服务
        startService();
    }

    @Override
    public Endpoint endpoint() {
        return Endpoint.of(localIp, 80);
    }

    // 开始对外提供服务
    abstract void startService();

    public AbstractService atRegion(Region region) {
        this.region = region;
        return this;
    }

    public AbstractService withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public AbstractService withLoad(int load) {
        this.load = load;
        return this;
    }

    public AbstractService withRegistryEndpoint(Endpoint registryEndpoint) {
        this.registryEndpoint = registryEndpoint;
        return this;
    }

}
