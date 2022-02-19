package com.yrunz.designpattern.service.shopping;

import com.yrunz.designpattern.network.Endpoint;
import com.yrunz.designpattern.network.SocketImpl;
import com.yrunz.designpattern.network.http.HttpClient;
import com.yrunz.designpattern.network.http.HttpMethod;
import com.yrunz.designpattern.network.http.HttpReq;

// 商城消费者
public class Consumer {

    private final String name;
    private String localIp;
    private Endpoint shoppingCenterEndpoint;

    private Consumer(String name) {
        this.name = name;
    }

    public static Consumer name(String name) {
        return new Consumer(name);
    }

    public Consumer useMobilePhone(String localIp) {
        this.localIp = localIp;
        return this;
    }

    public Consumer loginShoppingCenter(Endpoint shoppingCenterEndpoint) {
        this.shoppingCenterEndpoint = shoppingCenterEndpoint;
        return this;
    }

    public void buyGood(String good) {
        HttpClient client = HttpClient.of(new SocketImpl(), localIp);
        HttpReq req = HttpReq.empty()
                .addMethod(HttpMethod.POST)
                .addUri("/shopping-center/api/v1/good")
                .addHeader("user", name)
                .addHeader("good", good);

        client.sendReq(shoppingCenterEndpoint, req);
        client.close();
    }
}
