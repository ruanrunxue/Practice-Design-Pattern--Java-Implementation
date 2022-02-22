package com.yrunz.designpattern.monitor.schema;

import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.db.exception.UpdateOperationNotSupportException;
import com.yrunz.designpattern.db.iterator.RandomIterator;
import com.yrunz.designpattern.db.PrimaryKey;
import com.yrunz.designpattern.monitor.entity.MonitorEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// 监控事件表定义
public class MonitorEventTable implements Table<String, MonitorEvent> {
    private final String name;
    // 使用HashMap存储表记录，key为MonitorEvent.Id, value为MonitorEventTable.Record
    private final Map<String, Record> records;

    private MonitorEventTable(String name) {
        this.name = name;
        this.records = new HashMap<>();
    }

    public static MonitorEventTable of(String name) {
        return new MonitorEventTable(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Optional<MonitorEvent> query(String eventId) {
        if (!records.containsKey(eventId)) {
            return Optional.empty();
        }
        Record record = records.get(eventId);
        return Optional.of(record.toMonitorEvent());
    }

    // 插入表记录
    @Override
    public void insert(String eventId, MonitorEvent event) {
        if (records.containsKey(eventId)) {
            throw new RecordAlreadyExistException(eventId);
        }
        records.put(eventId, Record.from(event));
    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(String eventId, MonitorEvent event) {
        throw new UpdateOperationNotSupportException(name);
    }

    // 删除表记录
    @Override
    public void delete(String eventId) {
        if (!records.containsKey(eventId)) {
            throw new RecordNotFoundException(eventId);
        }
        records.remove(eventId);
    }

    @Override
    public TableIterator<MonitorEvent> iterator() {
        List<MonitorEvent> events = records.values()
                .stream()
                .map(Record::toMonitorEvent)
                .collect(Collectors.toList());
        return new RandomIterator<>(events);
    }

    @Override
    public List<MonitorEvent> accept(TableVisitor<MonitorEvent> visitor) {
        return visitor.visit(this);
    }

    // MonitorEventTable表结构定义
    private static class Record {
        @PrimaryKey(fieldName = "eventId")
        private String eventId;
        private String serviceId;
        private String eventType;
        private long timestamp;

        private Record() {}

        public static Record from(MonitorEvent event) {
            Record record = new Record();
            record.eventId = event.id();
            record.serviceId = event.serviceId();
            record.eventType = event.eventType().name();
            record.timestamp = event.timestamp();
            return record;
        }

        public MonitorEvent toMonitorEvent() {
            return MonitorEvent.of(eventId, serviceId, MonitorEvent.Type.valueOf(eventType), timestamp);
        }
    }

}
