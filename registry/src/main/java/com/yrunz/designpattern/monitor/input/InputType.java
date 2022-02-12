package com.yrunz.designpattern.monitor.input;

public enum InputType {
    MEMORY_MQ("com.yrunz.designpattern.monitor.input.MemoryMqInput"),
    SOCKET("com.yrunz.designpattern.monitor.input.SocketInput");

    private final String classPath;

    InputType(String classPath) {
        this.classPath = classPath;
    }

    public String classPath() {
        return classPath;
    }
}
