package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.domain.Endpoint;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerTest {

    @Test
    public void testHttp() {
        HttpServer server = HttpServer.of("192.168.10.1", 80)
                .get("/hello", req -> HttpResp.of(req.reqId()).addStatusCode(204));
        server.start();

        HttpClient client = HttpClient.of("192.168.10.2");
        HttpReq req = HttpReq.empty().addMethod(HttpMethod.GET).addUri("/hello");
        HttpResp resp = client.sendReq(Endpoint.of("192.168.10.1", 80), req);
        assertEquals(204, resp.statusCode());
        assertEquals(req.reqId(), resp.reqid());
        client.close();

        server.shutdown();
    }

}