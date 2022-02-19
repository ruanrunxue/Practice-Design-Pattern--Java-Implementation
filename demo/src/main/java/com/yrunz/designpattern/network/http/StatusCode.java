package com.yrunz.designpattern.network.http;

public enum StatusCode {
    CREATE(201, "Create"),
    OK(200, "OK"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOW(405, "Method Not Allow"),
    TOO_MANY_REQUEST(429, "Too Many Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    private final int val;
    private final String msg;

    StatusCode(int val, String msg) {
        this.val = val;
        this.msg = msg;
    }

    public int value() {
        return val;
    }

    public String message() {
        return msg;
    }
}
