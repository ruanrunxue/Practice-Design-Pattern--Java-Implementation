package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.flowctrl.FcContext;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketImpl;
import com.yrunz.designpattern.network.http.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest(FcContext.class)
@RunWith(PowerMockRunner.class)
public class FlowCtrlSidecarTest {

    @Test
    public void testReceive() {
        PowerMockito.mockStatic(FcContext.class);
        FcContext mockContext = PowerMockito.mock(FcContext.class);
        Mockito.when(FcContext.create()).thenReturn(mockContext);
        PowerMockito.when(mockContext.tryAccept()).thenReturn(false);

        Socket socket = new FlowCtrlSidecar(new SocketImpl());
        HttpServer server = HttpServer.of(socket)
                .get("/hello", req-> HttpResp.of(req.reqId()).addStatusCode(StatusCode.NO_CONTENT))
                .listen("192.168.19.1", 80);
        server.start();
        HttpReq httpReq = HttpReq.empty()
                .addMethod(HttpMethod.GET)
                .addUri("/hello");
        HttpClient client = HttpClient.of(new SocketImpl(), "192.168.19.2");
        HttpResp resp = client.sendReq(Endpoint.of("192.168.19.1", 80), httpReq);
        Assert.assertEquals(StatusCode.TOO_MANY_REQUEST, resp.statusCode());

        server.shutdown();
        client.close();
    }

}