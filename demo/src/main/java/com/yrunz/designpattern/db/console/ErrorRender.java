package com.yrunz.designpattern.db.console;

public class ErrorRender implements DbConsoleRender {

    private final String message;

    private ErrorRender(String message) {
        this.message = message;
    }

    public static ErrorRender of(String message) {
        return new ErrorRender(message);
    }

    @Override
    public String render() {
        return this.message;
    }

}
