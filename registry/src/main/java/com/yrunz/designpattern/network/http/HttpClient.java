package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.domain.Endpoint;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketData;
import com.yrunz.designpattern.network.SocketListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private HttpClient(Endpoint localEndpoint) {
        this.socket = new Socket();
        this.localEndpoint = localEndpoint;
        this.socket.listen(localEndpoint);
        this.waitResps = new ConcurrentHashMap<>();
    }

    public static HttpClient of(String ip) {
        Random random = new Random();
        random.setSeed(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getNano());
        // 随机端口，从10000 ～ 19999
        Endpoint endpoint = Endpoint.of(ip, random.nextInt(10000) + 10000);
        HttpClient client = new HttpClient(endpoint);
        client.socket.addListener(client);
        return client;
    }

    public HttpResp sendReq(Endpoint dest, HttpReq req) {
        try {
            RespStatus respStatus = new RespStatus();
            waitResps.put(req.reqId(), respStatus);
            SocketData socketData = SocketData.of(localEndpoint, dest, req);
            socket.send(socketData);
            synchronized (respStatus) {
                while (!respStatus.isDone()) {
                    respStatus.wait(5 * 1000);
                }
            }
            return respStatus.resp;
        } catch (Exception e) {
            throw new HttpReqFailedException(e.getMessage());
        }
    }

    public void close() {
        socket.close(localEndpoint);
    }

    @Override
    public void handle(SocketData socketData) {
        HttpResp resp = (HttpResp) socketData.payload();
        RespStatus respStatus = waitResps.get(resp.reqid());
        synchronized (respStatus) {
            respStatus.resp = resp;
            respStatus.setDone();
            respStatus.notify();
        }
    }

    // 用于辅助同步等待对端响应
    private static class RespStatus {
        HttpResp resp;
        AtomicBoolean isDone;

        RespStatus() {
            isDone = new AtomicBoolean(false);
        }

        boolean isDone() {
            return isDone.get();
        }

        void setDone() {
            this.isDone.set(true);
        }

    }
}
