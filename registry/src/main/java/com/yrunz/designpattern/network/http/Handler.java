package com.yrunz.designpattern.network.http;

// Http请求处理者接口
public interface Handler {
    HttpResp handle(HttpReq req);
}
