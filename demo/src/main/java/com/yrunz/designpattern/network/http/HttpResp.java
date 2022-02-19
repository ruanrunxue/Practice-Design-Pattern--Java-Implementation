package com.yrunz.designpattern.network.http;

import com.yrunz.designpattern.service.mediator.ServiceDiscoveryFailedException;

import java.util.HashMap;
import java.util.Map;

public class HttpResp {
    private final int reqId;
    private StatusCode statusCode;
    private final Map<String, String> headers;
    private Object body;
    private String problemDetails;

    private HttpResp(int reqId) {
        this.reqId = reqId;
        this.problemDetails = "";
        this.headers = new HashMap<>();
    }

    public static HttpResp of(int reqId) {
        return new HttpResp(reqId);
    }

    public HttpResp addStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResp addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpResp addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpResp addBody(Object body) {
        this.body = body;
        return this;
    }

    public HttpResp addProblemDetails(String problemDetails) {
        this.problemDetails = problemDetails;
        return this;
    }

    public int reqId() {
        return reqId;
    }

    public StatusCode statusCode() {
        return statusCode;
    }

    public String header(String key) {
        return headers.get(key);
    }

    public Map<String, String> headers() {
        return this.headers;
    }

    public Object body() {
        return body;
    }

    public String problemDetails() {
        return problemDetails;
    }

    public boolean isSuccess() {
        return statusCode.value() / 100 == 2;
    }

}
