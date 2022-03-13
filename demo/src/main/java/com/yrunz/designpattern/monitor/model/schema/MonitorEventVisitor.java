package com.yrunz.designpattern.monitor.model.schema;

import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.monitor.model.MonitorEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MonitorEventVisitor implements TableVisitor<MonitorEvent> {
    private String serviceId;
    private MonitorEvent.Type eventType;
    // 时间戳，格式为yyyy-MM-dd HH:mm:ss
    private String beginTimestamp;
    private String endTimestamp;

    private MonitorEventVisitor() {
        serviceId = "";
        beginTimestamp = "";
        endTimestamp = "";
    }

    public static MonitorEventVisitor create() {
        return new MonitorEventVisitor();
    }

    public MonitorEventVisitor withServiceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public MonitorEventVisitor withEventType(MonitorEvent.Type eventType) {
        this.eventType = eventType;
        return this;
    }

    // 时间格式为 yyyy-MM-dd HH:mm:ss
    public MonitorEventVisitor withBeginTimestamp(String beginTimestamp) {
        this.beginTimestamp = beginTimestamp;
        return this;
    }

    // 时间格式为 yyyy-MM-dd HH:mm:ss
    public MonitorEventVisitor withEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
        return this;
    }

    @Override
    public List<MonitorEvent> visit(Table<?, MonitorEvent> table) {
        List<MonitorEvent> result = new ArrayList<>();
        TableIterator<MonitorEvent> iterator = table.iterator();
        while (iterator.hasNext()) {
            MonitorEvent event = iterator.next();
            if (isMatchServiceId(event) && isMatchEventType(event) &&
                    isMatchBeginTimestamp(event) && isMatchEndTimestamp(event)) {
                result.add(event);
            }
        }
        return result;
    }

    private boolean isMatchServiceId(MonitorEvent event) {
        if (serviceId.equals("")) {
            return true;
        }
        return serviceId.equals(event.serviceId());
    }

    private boolean isMatchEventType(MonitorEvent event) {
        if (eventType == null) {
            return true;
        }
        return eventType.equals(event.eventType());
    }

    private boolean isMatchBeginTimestamp(MonitorEvent event) {
        if (beginTimestamp.equals("")) {
            return true;
        }
        LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime begin = LocalDateTime.parse(beginTimestamp, formatter);
        Instant time = Instant.ofEpochSecond(event.timestamp());
        return time.isAfter(begin.atZone(ZoneId.systemDefault()).toInstant());
    }

    private boolean isMatchEndTimestamp(MonitorEvent event) {
        if (endTimestamp.equals("")) {
            return true;
        }
        LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime end = LocalDateTime.parse(endTimestamp, formatter);
        Instant time = Instant.ofEpochSecond(event.timestamp());
        return time.isBefore(end.atZone(ZoneId.systemDefault()).toInstant());
    }

}