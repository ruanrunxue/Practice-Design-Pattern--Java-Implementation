package com.yrunz.designpattern.monitor.exception;

public class CreatePluginException extends RuntimeException {
    public CreatePluginException(String pluginName, String cause) {
        super("create plugin " + pluginName + " failed: " + cause);
    }
}
