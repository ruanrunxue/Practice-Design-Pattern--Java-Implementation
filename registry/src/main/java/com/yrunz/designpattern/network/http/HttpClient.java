package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.domain.Endpoint;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.Packet;
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
    private Endpoint localEndpoint;
    // key为reqId，value为WaitHttpResp，用于同步等待对端响应
    private final Map<Integer, RespStatus> waitResps;

    private HttpClient(Socket socket) {
        this.socket = socket;
        this.socket.listen(localEndpoint);
        this.waitResps = new ConcurrentHashMap<>();
    }

    public static HttpClient of(Socket socket) {
        HttpClient client = new HttpClient(socket);
        client.socket.addListener(client);
        return client;
    }

    public HttpClient withIp(String ip) {
        Random random = new Random();
        random.setSeed(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getNano());
        // 随机端口，从10000 ～ 19999
        this.localEndpoint = Endpoint.of(ip, random.nextInt(10000) + 10000);
        this.socket.listen(this.localEndpoint);
        return this;
    }

    public HttpResp sendReq(Endpoint dest, HttpReq req) {
        try {
            RespStatus respStatus = new RespStatus();
            waitResps.put(req.reqId(), respStatus);
            Packet packet = Packet.of(localEndpoint, dest, req);
            socket.send(packet);
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
    public void handle(Packet packet) {
        HttpResp resp = (HttpResp) packet.payload();
        RespStatus respStatus = waitResps.get(resp.reqId());
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
