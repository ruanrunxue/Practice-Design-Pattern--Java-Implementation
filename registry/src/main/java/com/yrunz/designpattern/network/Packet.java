package com.yrunz.designpattern.network;

import com.yrunz.designpattern.domain.Endpoint;

// 网络通信数据
public class Packet {
    private final Endpoint src;
    private final Endpoint dest;
    private final Object payload;

    private Packet(Endpoint src, Endpoint dest, Object payload) {
        this.src = src;
        this.dest = dest;
        this.payload = payload;
    }

    public static Packet of(Endpoint src, Endpoint dest, Object payload) {
        return new Packet(src, dest, payload);
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
