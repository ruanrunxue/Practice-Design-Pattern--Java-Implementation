package com.yrunz.designpattern.mediator;

import com.yrunz.designpattern.network.http.HttpReq;
import com.yrunz.designpattern.network.http.HttpResp;

/**
 * 中介者模式
 */

// HTTP请求/响应转发中介
public interface Mediator {
    // 转发请求，返回对端响应
    HttpResp forward(HttpReq req);
}
