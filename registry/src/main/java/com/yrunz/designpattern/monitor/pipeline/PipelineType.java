package com.yrunz.designpattern.monitor.pipeline;

public enum PipelineType {

    SINGLE_THREAD("com.yrunz.designpattern.monitor.pipeline.SingleThreadPipeline"),
    THREAD_POOL("com.yrunz.designpattern.monitor.pipeline.ThreadPoolPipeline");

    private final String classPath;

    PipelineType(String classPath) {
        this.classPath = classPath;
    }

    public String classPath() {
        return classPath;
    }

}
