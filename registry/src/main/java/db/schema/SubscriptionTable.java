package db.schema;

import db.Table;
import db.TableIterator;
import db.exception.RecordAlreadyExistException;
import db.exception.RecordNotFoundException;
import db.iterator.SortedIterator;
import domain.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// 服务订阅表定义
public class SubscriptionTable implements Table<Integer, Subscription> {
    private final String name;
    // 使用HashMap存储表记录，key为Subscription.Id, value为SubscriptionTable.Record
    private final Map<Integer, Record> records;

    private SubscriptionTable(String name) {
        this.name = name;
        this.records = new HashMap<>();
    }

    public static SubscriptionTable of(String name) {
        return new SubscriptionTable(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Optional<Subscription> query(Integer subscriptionId) {
        if (!records.containsKey(subscriptionId)) {
            return Optional.empty();
        }
        Record record = records.get(subscriptionId);
        return Optional.of(record.toSubscription());
    }

    // 插入表记录
    @Override
    public void insert(Integer subscriptionId, Subscription subscription) {
        if (records.containsKey(subscriptionId)) {
            throw new RecordAlreadyExistException(subscriptionId.toString());
        }
        records.put(subscriptionId, Record.from(subscription));
    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(Integer subscriptionId, Subscription newSubscription) {
        if (!records.containsKey(subscriptionId)) {
            throw new RecordNotFoundException(subscriptionId.toString());
        }
        records.replace(subscriptionId, Record.from(newSubscription));
    }

    // 删除表记录
    @Override
    public void delete(Integer subscriptionId) {
        if (!records.containsKey(subscriptionId)) {
            throw new RecordNotFoundException(subscriptionId.toString());
        }
        records.remove(subscriptionId);
    }

    @Override
    public TableIterator<Subscription> iterator() {
        List<Subscription> subscriptions = records.values()
                .stream()
                .map(Record::toSubscription)
                .collect(Collectors.toList());
        return new SortedIterator<>(subscriptions);
    }


    // 服务订阅表结构定义，其中subscriptionId为主键
    private static class Record {
        @PrimaryKey(fieldName = "subscriptionId")
        private String subscriptionId;
        private String srcServiceId;
        private String targetServiceId;
        private String targetServiceType;

        private Record() {}

        public static Record from(Subscription subscription) {
            Record record = new Record();
            record.subscriptionId = subscription.id();
            record.srcServiceId = subscription.srcServiceId();
            record.targetServiceId = subscription.targetServiceId();
            record.targetServiceType = subscription.targetServiceType();
            return record;
        }

        public Subscription toSubscription() {
            return Subscription.of(subscriptionId)
                    .withSrcServiceId(srcServiceId)
                    .withTargetServiceId(targetServiceId)
                    .withTargetServiceType(targetServiceType);
        }
    }
}