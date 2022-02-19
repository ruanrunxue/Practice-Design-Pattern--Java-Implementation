package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.Packet;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketListener;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

// HTTP客户端，结束后必须调用close方法释放资源
public class HttpClient implements SocketListener {

    private final Socket socket;
    private final Endpoint localEndpoint;
    // key为reqId，value为WaitHttpResp，用于同步等待对端响应
    private final Map<Integer, RespStatus> waitResps;

    private HttpClient(Socket socket, Endpoint localEndpoint) {
        this.localEndpoint = localEndpoint;
        this.socket = socket;
        this.waitResps = new ConcurrentHashMap<>();
    }

    public static HttpClient of(Socket socket, String ip) {
        Random random = new Random();
        random.setSeed(Instant.now().getNano());
        // 随机端口，从10000 ～ 19999
        Endpoint localEndpoint = Endpoint.of(ip, random.nextInt(10000) + 10000);
        HttpClient client = new HttpClient(socket, localEndpoint);
        client.socket.addListener(client);
        client.socket.listen(localEndpoint);
        return client;
    }

    public HttpResp sendReq(Endpoint dest, HttpReq req) {
        try {
            RespStatus respStatus = new RespStatus();
            waitResps.put(req.reqId(), respStatus);
            Packet packet = Packet.of(localEndpoint, dest, req);
            socket.send(packet);
            synchronized (respStatus) {
                while (!respStatus.isDone()) {
                    long startTime = Instant.now().getEpochSecond();
                    respStatus.wait(5 * 1000000);
                    long endTime = Instant.now().getEpochSecond();
                    if (endTime - startTime > 5) {
                        respStatus.setError();
                    }
                }
            }
            if (respStatus.isError()) {
                return HttpResp.of(req.reqId()).addStatusCode(StatusCode.GATEWAY_TIMEOUT);
            }
            return respStatus.resp;
        } catch (Exception e) {
            return HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.INTERNAL_SERVER_ERROR)
                    .addProblemDetails(e.getMessage());
        }
    }

    public void close() {
        socket.close(localEndpoint);
    }

    @Override
    public void handle(Packet packet) {
        HttpResp resp = (HttpResp) packet.payload();
        RespStatus respStatus = waitResps.get(resp.reqId());
        if (respStatus == null) {
            System.out.printf("receive resp with an unknown reqId %d.\n", resp.reqId());
            return;
        }
        synchronized (respStatus) {
            respStatus.resp = resp;
            respStatus.setDone();
            respStatus.notify();
        }
        waitResps.remove(resp.reqId());
    }

    // 用于辅助同步等待对端响应
    private static class RespStatus {
        HttpResp resp;
        AtomicBoolean isDone;
        AtomicBoolean isError;

        RespStatus() {
            isDone = new AtomicBoolean(false);
            isError = new AtomicBoolean(false);
        }

        boolean isDone() {
            return isDone.get();
        }

        void setDone() {
            this.isDone.set(true);
        }

        boolean isError() {
            return this.isError.get();
        }

        void setError() {
            setDone();
            this.isError.set(true);
        }

    }
}
