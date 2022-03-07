package com.yrunz.designpattern.service.mediator;

import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.http.*;
import com.yrunz.designpattern.service.Service;
import com.yrunz.designpattern.sidecar.SidecarFactory;

// 服务中介，根据mediator-uri，向Registry发现对端地址，转发请求
// 其中，mediator-uri的形式为/{serviceType}+ServiceUri
public class ServiceMediator implements Mediator, Service {
    private final Endpoint registryEndpoint;
    private final String localIp;
    private final HttpServer httpServer;
    private final SidecarFactory sidecarFactory;

    private ServiceMediator(String localIp, Endpoint registryEndpoint, SidecarFactory sidecarFactory) {
        this.sidecarFactory = sidecarFactory;
        this.localIp = localIp;
        this.httpServer = HttpServer.of(sidecarFactory.create()).listen(localIp, 80);
        this.registryEndpoint = registryEndpoint;
    }

    public static ServiceMediator of(String localIp, Endpoint registryEndpoint, SidecarFactory sidecarFactory) {
        return new ServiceMediator(localIp, registryEndpoint, sidecarFactory);
    }

    @Override
    public void run() {
        httpServer.put("/", this::forward)
                .post("/", this::forward)
                .delete("/", this::forward)
                .get("/", this::forward)
                .start();
    }

    // 请求URL为 /{serviceType}+ServiceUri 的形式，如/serviceA/api/v1/task
    @Override
    public HttpResp forward(HttpReq req) {
        try {
            String serviceType = serviceTypeOf(req.uri());
            Endpoint dest = discovery(serviceType);
            String serviceUri = serviceUriOf(req.uri());

            HttpClient client = HttpClient.of(sidecarFactory.create(), localIp);
            HttpReq forwardReq = HttpReq.empty()
                    .addUri(serviceUri)
                    .addMethod(req.method())
                    .addHeaders(req.headers())
                    .addQueryParams(req.queryParams())
                    .addBody(req.body());
            req.addUri(serviceUri);
            HttpResp forwardResp = client.sendReq(dest, forwardReq);
            client.close();
            return HttpResp.of(req.reqId())
                    .addHeaders(forwardResp.headers())
                    .addStatusCode(forwardResp.statusCode())
                    .addBody(forwardResp.body())
                    .addProblemDetails(forwardResp.problemDetails());
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
    }

    @Override
    public Endpoint endpoint() {
        return Endpoint.of(localIp, 80);
    }

    private String serviceTypeOf(String mediatorUri) {
        return mediatorUri.split("/")[1];
    }

    private String serviceUriOf(String mediatorUri) {
        int startIdx = mediatorUri.substring(1).indexOf("/");
        return mediatorUri.substring(startIdx+1);
    }

    // 根据serviceType进行服务发现
    private Endpoint discovery(String serviceType) {
        HttpClient client = HttpClient.of(sidecarFactory.create(), localIp);
        HttpReq req = HttpReq.empty()
                .addUri("/api/v1/service-profile")
                .addMethod(HttpMethod.GET)
                .addQueryParam("serviceType", serviceType);
        HttpResp resp = client.sendReq(registryEndpoint, req);
        client.close();
        // 如果响应码不为2xx，表示服务发现失败
        if (!resp.isSuccess()) {
            throw new ServiceDiscoveryFailedException(serviceType, resp.problemDetails());
        }
        ServiceProfile profile = (ServiceProfile) resp.body();
        return profile.endpoint();
    }

}
