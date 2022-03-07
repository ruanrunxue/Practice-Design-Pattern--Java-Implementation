package com.yrunz.designpattern.network.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HttpReq {
    private final int reqId;
    private HttpMethod method;
    private String uri;
    private final Map<String, String> queryParams;
    private final Map<String, String> headers;
    private Object body;

    private HttpReq(int reqId) {
        this.reqId = reqId;
        this.queryParams = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public static HttpReq empty() {
        Random random = new Random();
        return new HttpReq(random.nextInt(10000));
    }

    public HttpReq addMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpReq addUri(String uri) {
        this.uri = uri;
        return this;
    }

    public HttpReq addQueryParam(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    public HttpReq addQueryParams(Map<String, String> queryParams) {
        this.queryParams.putAll(queryParams);
        return this;
    }

    public HttpReq addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpReq addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpReq addBody(Object body) {
        this.body = body;
        return this;
    }

    public int reqId() {
        return reqId;
    }

    public HttpMethod method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public String queryParam(String key) {
        return queryParams.get(key);
    }

    public Map<String, String> queryParams() {
        return queryParams;
    }

    public String header(String key) {
        return headers.get(key);
    }

    public Map<String, String> headers() {
        return headers;
    }

    public Object body() {
        return body;
    }

    public boolean isInvalid() {
        return method != null && uri.equals("");
    }

}
