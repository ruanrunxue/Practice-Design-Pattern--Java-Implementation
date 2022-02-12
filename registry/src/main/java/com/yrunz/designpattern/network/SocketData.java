package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

// 网络通信数据
public class SocketData {
    private final Endpoint src;
    private final Endpoint dest;
    private final Object payload;

    private SocketData(Endpoint src, Endpoint dest, Object payload) {
        this.src = src;
        this.dest = dest;
        this.payload = payload;
    }

    public static SocketData of(Endpoint src, Endpoint dest, Object payload) {
        return new SocketData(src, dest, payload);
    }

    public Endpoint src() {
        return src;
    }

    public Endpoint dest() {
        return dest;
    }

    public Object payload() {
        return payload;
    }
}
