package com.yrunz.designpattern.monitor.plugin;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private final Map<String, String> header;
    private final Object payload;

    private Event(Map<String, String> header, Object payload) {
        this.header = header;
        this.payload = payload;
    }

    public static Event of(Map<String, String> header, Object payload) {
        return new Event(header, payload);
    }

    public static Event of(Object payload) {
        return new Event(new HashMap<>(), payload);
    }

    public Map<String, String> header() {
        return header;
    }

    public Object payload() {
        return payload;
    }
}
