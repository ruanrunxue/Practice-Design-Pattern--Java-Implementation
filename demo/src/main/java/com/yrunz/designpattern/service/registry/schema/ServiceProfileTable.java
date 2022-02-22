package com.yrunz.designpattern.service.registry.schema;

import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.db.iterator.SortedIterator;
import com.yrunz.designpattern.db.PrimaryKey;
import com.yrunz.designpattern.service.registry.entity.ServiceProfile;
import com.yrunz.designpattern.service.registry.entity.ServiceStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Service Profile 表定义，主键为Service Id
public class ServiceProfileTable implements Table<String, ServiceProfile> {
    private final String name;
    // 使用HashMap存储表记录，key为ServiceProfile.id, value为ServiceProfile.Record
    private final Map<String, Record> records;

    private ServiceProfileTable(String name) {
        this.name = name;
        this.records = new HashMap<>();
    }

    public static ServiceProfileTable of(String name) {
        return new ServiceProfileTable(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Optional<ServiceProfile> query(String serviceId) {
        if (!records.containsKey(serviceId)) {
            return Optional.empty();
        }
        Record record = records.get(serviceId);
        return Optional.of(record.toServiceProfile());
    }

    // 插入表记录
    @Override
    public void insert(String serviceId, ServiceProfile profile) {
        if (records.containsKey(serviceId)) {
            throw new RecordAlreadyExistException(serviceId);
        }
        records.put(serviceId, Record.from(profile));
    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(String serviceId, ServiceProfile newProfile) {
        if (!records.containsKey(serviceId)) {
            throw new RecordNotFoundException(serviceId);
        }
        records.replace(serviceId, Record.from(newProfile));
    }

    // 删除表记录
    @Override
    public void delete(String serviceId) {
        if (!records.containsKey(serviceId)) {
            throw new RecordNotFoundException(serviceId);
        }
        records.remove(serviceId);
    }

    @Override
    public TableIterator<ServiceProfile> iterator() {
        List<ServiceProfile> profiles = records.values()
                .stream()
                .map(Record::toServiceProfile)
                .collect(Collectors.toList());
        return new SortedIterator<>(profiles);
    }

    @Override
    public List<ServiceProfile> accept(TableVisitor<ServiceProfile> visitor) {
        return visitor.visit(this);
    }


    // 表结构定义
    private static class Record {
        @PrimaryKey(fieldName = "serviceId")
        private String serviceId;
        private String serviceType;
        private String serviceStatus;
        private String ip;
        private int port;
        // 享元模式 关键点2：通过regionId共享享元对象
        private String regionId;
        private int priority;
        private int load;

        private Record() {
        }

        public static Record from(ServiceProfile profile) {
            Record record = new Record();
            record.serviceId = profile.id();
            record.serviceType = profile.type();
            record.serviceStatus = profile.status().name();
            record.ip = profile.endpoint().ip();
            record.port = profile.endpoint().port();
            record.regionId = profile.region().id();
            record.priority = profile.priority();
            record.load = profile.load();
            return record;
        }

        public ServiceProfile toServiceProfile() {
            return ServiceProfile.builder(serviceId)
                    .withType(serviceType)
                    .withStatus(ServiceStatus.valueOf(serviceStatus))
                    .withEndpoint(ip, port)
                    .withRegionId(regionId)
                    .withPriority(priority)
                    .withLoad(load)
                    .build();
        }
    }

}
