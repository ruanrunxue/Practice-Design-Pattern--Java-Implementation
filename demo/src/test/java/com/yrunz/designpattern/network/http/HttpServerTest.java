package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.SocketImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerTest {

    @Test
    public void testHttp() {
        HttpServer server = HttpServer.of(new SocketImpl())
                .listen("192.168.10.1", 80)
                .get("/hello", req -> HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT));
        server.start();

        HttpClient client = HttpClient.of(new SocketImpl()).withIp("192.168.10.2");
        HttpReq req = HttpReq.empty().addMethod(HttpMethod.GET).addUri("/hello");
        HttpResp resp = client.sendReq(Endpoint.of("192.168.10.1", 80), req);
        assertEquals(StatusCode.NO_CONTENT, resp.statusCode());
        assertEquals(req.reqId(), resp.reqId());
        client.close();

        server.shutdown();
    }

}