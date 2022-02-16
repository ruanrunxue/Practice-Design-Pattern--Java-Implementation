package com.yrunz.designpattern.sidecar;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.flowctrl.FcContext;
import com.yrunz.designpattern.network.Network;
import com.yrunz.designpattern.network.Packet;
import com.yrunz.designpattern.network.Socket;
import com.yrunz.designpattern.network.SocketListener;
import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;
import com.yrunz.designpattern.network.http.StatusCode;

/**
 * 装饰者模式
 */

// HTTP接收端流控功能装饰器，自动拦截Socket接收报文，实现流控功能
public class FlowCtrlSidecar implements Socket {
    private final Socket socket;
    // 流控上下文
    private final FcContext context;

    public FlowCtrlSidecar(Socket socket) {
        this.socket = socket;
        this.context = FcContext.create();
    }

    @Override
    public void listen(Endpoint endpoint) {
        Network.instance().listen(endpoint, this);;
    }

    @Override
    public void close(Endpoint endpoint) {
        socket.close(endpoint);
    }

    @Override
    public void send(Packet packet) {
        socket.send(packet);
    }

    @Override
    public void receive(Packet packet) {
        // 如果不是HTTP请求，则不做流控处理
        if (!(packet.payload() instanceof HttpReq)) {
            socket.receive(packet);
            return;
        }
        HttpReq req = (HttpReq) packet.payload();
        // 流控后返回429 Too Many Request响应
        if (!context.tryAccept()) {
            HttpResp resp = HttpResp.of(req.reqId())
                    .addStatusCode(StatusCode.TOO_MANY_REQUEST)
                    .addProblemDetails(StatusCode.TOO_MANY_REQUEST.message());
            socket.send(Packet.of(packet.dest(), packet.src(), resp));
            return;
        }
        socket.receive(packet);
    }

    @Override
    public void addListener(SocketListener listener) {
        socket.addListener(listener);
    }
}
