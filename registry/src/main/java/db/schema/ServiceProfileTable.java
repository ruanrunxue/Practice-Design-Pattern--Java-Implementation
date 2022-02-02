package db.schema;

import db.Table;
import db.exception.RecordAlreadyExistException;
import db.exception.RecordNotFoundException;
import domain.ServiceProfile;
import domain.ServiceStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Service Profile 表定义，主键为Service Id
public class ServiceProfileTable implements Table<String, ServiceProfile> {
    private final String name;
    // 使用HashMap存储表记录，key为ServiceProfile.id, value为ServiceProfile.Record
    private final Map<String, Record> profiles;

    private ServiceProfileTable(String name) {
        this.name = name;
        this.profiles = new HashMap<>();
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
        if (!profiles.containsKey(serviceId)) {
            return Optional.empty();
        }
        Record record = profiles.get(serviceId);
        return Optional.of(record.toServiceProfile());
    }

    // 插入表记录
    @Override
    public void insert(String serviceId, ServiceProfile profile) {
        if (profiles.containsKey(serviceId)) {
            throw new RecordAlreadyExistException(serviceId);
        }
        profiles.put(serviceId, Record.from(profile));
    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(String serviceId, ServiceProfile newProfile) {
        if (!profiles.containsKey(serviceId)) {
            throw new RecordNotFoundException(serviceId);
        }
        profiles.replace(serviceId, Record.from(newProfile));
    }

    // 删除表记录
    public void delete(String serviceId) {
        if (!profiles.containsKey(serviceId)) {
            throw new RecordNotFoundException(serviceId);
        }
        profiles.remove(serviceId);
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
        private int regionId;
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
            return ServiceProfile.Builder(serviceId)
                    .withType(serviceType)
                    .withStatus(ServiceStatus.valueOf(serviceStatus))
                    .withEndpoint(ip, port)
                    .withRegionId(regionId)
                    .withPriority(priority)
                    .withLoad(load)
                    .Build();
        }
    }

}
