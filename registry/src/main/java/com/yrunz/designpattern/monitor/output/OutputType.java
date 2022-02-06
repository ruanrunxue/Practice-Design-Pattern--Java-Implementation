package com.yrunz.designpattern.monitor.output;

public enum OutputType {
    MEMORY_DB("com.yrunz.designpattern.monitor.input.MemoryDbOutput");

    private final String classPath;

    OutputType(String classPath) {
        this.classPath = classPath;
    }

    public String classPath() {
        return classPath;
    }

}
