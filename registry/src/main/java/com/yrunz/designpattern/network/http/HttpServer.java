package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.domain.Endpoint;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.Packet;
import com.yrunz.designpattern.network.SocketListener;

import java.util.HashMap;
import java.util.Map;

public class HttpServer implements SocketListener {

    private final Socket socket;
    private Endpoint localEndpoint;
    // 第一层key为HttpMethod，第二层key为uri，value为请求处理函数/实现类
    private final Map<HttpMethod, Map<String, Handler>> routers;

    private HttpServer(Socket socket) {
        this.socket = socket;
        this.routers = new HashMap<>();
    }

    public static HttpServer of(Socket socket) {
        HttpServer server = new HttpServer(socket);
        server.socket.addListener(server);
        return server;
    }

    public HttpServer listen(String ip, int port) {
        this.localEndpoint = Endpoint.of(ip, port);
        return this;
    }

    public HttpServer get(String uri, Handler handler) {
        routers.putIfAbsent(HttpMethod.GET, new HashMap<>());
        routers.get(HttpMethod.GET).put(uri, handler);
        return this;
    }

    public HttpServer post(String uri, Handler handler) {
        routers.putIfAbsent(HttpMethod.POST, new HashMap<>());
        routers.get(HttpMethod.POST).put(uri, handler);
        return this;
    }

    public HttpServer put(String uri, Handler handler) {
        routers.putIfAbsent(HttpMethod.PUT, new HashMap<>());
        routers.get(HttpMethod.PUT).put(uri, handler);
        return this;
    }

    public HttpServer delete(String uri, Handler handler) {
        routers.putIfAbsent(HttpMethod.DELETE, new HashMap<>());
        routers.get(HttpMethod.DELETE).put(uri, handler);
        return this;
    }

    public void start() {
        this.socket.listen(this.localEndpoint);
    }

    public void shutdown() {
        this.socket.close(this.localEndpoint);
    }

    @Override
    public void handle(Packet packet) {
        if (!(packet.payload() instanceof HttpReq)) {
            return;
        }

        HttpReq httpReq = (HttpReq) packet.payload();
        if (httpReq.isInvalid()) {
            HttpResp resp = HttpResp.of(httpReq.reqId())
                    .addStatusCode(StatusCode.BAD_REQUEST).addProblemDetails(StatusCode.BAD_REQUEST.message());
            socket.send(Packet.of(localEndpoint, packet.src(), resp));
            return;
        }

        if (!routers.containsKey(httpReq.method())) {
            HttpResp resp = HttpResp.of(httpReq.reqId())
                    .addStatusCode(StatusCode.METHOD_NOT_ALLOW).addProblemDetails(StatusCode.METHOD_NOT_ALLOW.message());
            socket.send(Packet.of(localEndpoint, packet.src(), resp));
            return;
        }

        Handler reqHandler = null;
        for (Map.Entry<String, Handler> entry : routers.get(httpReq.method()).entrySet()) {
            if (httpReq.uri().contains(entry.getKey())) {
                reqHandler = entry.getValue();
                break;
            }
        }
        if (reqHandler == null) {
            HttpResp resp = HttpResp.of(httpReq.reqId())
                    .addStatusCode(StatusCode.NOT_FOUND).addProblemDetails(StatusCode.NOT_FOUND.message());
            socket.send(Packet.of(localEndpoint, packet.src(), resp));
            return;
        }

        HttpResp resp = reqHandler.handle(httpReq);
        socket.send(Packet.of(localEndpoint, packet.src(), resp));
    }

}
