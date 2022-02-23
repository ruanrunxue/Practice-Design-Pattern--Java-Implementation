package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.mq.MemoryMq;
import com.yrunz.designpattern.mq.Message;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;
import com.yrunz.designpattern.network.http.*;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccessLogSidecarTest {

    @After
    public void tearDown() {
        MemoryMq.instance().clear();
    }

    @Test
    public void testLogMonitorSidecar() {
        Socket socket = AccessLogSidecar.wrap(FlowCtrlSidecar.wrap(new SocketImpl()), MemoryMq.instance());
        HttpServer server = HttpServer.of(socket)
                .get("/hello", req-> HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT))
                .listen("192.168.19.1", 80);
        server.start();
        HttpReq httpReq = HttpReq.empty()
                .addMethod(HttpMethod.GET)
                .addUri("/hello");
        HttpClient client = HttpClient.of(new SocketImpl(), "192.168.19.2");
        HttpResp resp = client.sendReq(Endpoint.of("192.168.19.1", 80), httpReq);
        assertEquals(StatusCode.NO_CONTENT, resp.statusCode());

        Message message = MemoryMq.instance().consume("access_log.topic");
        assertTrue(message.payload().contains("[192.168.19.1:80][RECV_REQ]receive http request from 192.168.19.2:"));
        Message respMessage = MemoryMq.instance().consume("access_log.topic");
        assertTrue(respMessage.payload().contains("[192.168.19.1:80][SEND_RESP]send http response to 192.168.19.2:"));

        server.shutdown();
        client.close();
    }

}