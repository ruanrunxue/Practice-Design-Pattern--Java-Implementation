package com.yrunz.designpattern.network.http;

public class HttpResp {
    private final int reqId;
    private StatusCode statusCode;
    private Object body;
    private String problemDetails;

    private HttpResp(int reqId) {
        this.reqId = reqId;
        this.problemDetails = "";
    }

    public static HttpResp of(int reqId) {
        return new HttpResp(reqId);
    }

    public HttpResp addStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
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

    public Object body() {
        return body;
    }

    public String problemDetails() {
        return problemDetails;
    }

}
