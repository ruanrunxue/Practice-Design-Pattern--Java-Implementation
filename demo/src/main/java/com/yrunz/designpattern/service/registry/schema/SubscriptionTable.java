package com.yrunz.designpattern.service.registry.schema;

import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.db.iterator.SortedIterator;
import com.yrunz.designpattern.db.PrimaryKey;
import com.yrunz.designpattern.service.registry.entity.Subscription;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// 服务订阅表定义
public class SubscriptionTable implements Table<String, Subscription> {
    private final String name;
    // 使用HashMap存储表记录，key为Subscription.Id, value为SubscriptionTable.Record
    private final Map<String, Record> records;

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
    public Optional<Subscription> query(String subscriptionId) {
        if (!records.containsKey(subscriptionId)) {
            return Optional.empty();
        }
        Record record = records.get(subscriptionId);
        return Optional.of(record.toSubscription());
    }

    // 插入表记录
    @Override
    public void insert(String subscriptionId, Subscription subscription) {
        if (records.containsKey(subscriptionId)) {
            throw new RecordAlreadyExistException(subscriptionId);
        }
        records.put(subscriptionId, Record.from(subscription));
    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(String subscriptionId, Subscription newSubscription) {
        if (!records.containsKey(subscriptionId)) {
            throw new RecordNotFoundException(subscriptionId);
        }
        records.replace(subscriptionId, Record.from(newSubscription));
    }

    // 删除表记录
    @Override
    public void delete(String subscriptionId) {
        if (!records.containsKey(subscriptionId)) {
            throw new RecordNotFoundException(subscriptionId);
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

    @Override
    public List<Subscription> accept(TableVisitor<Subscription> visitor) {
        return visitor.visit(this);
    }


    // 服务订阅表结构定义，其中subscriptionId为主键
    private static class Record {
        @PrimaryKey(fieldName = "subscriptionId")
        private String subscriptionId;
        private String srcServiceId;
        private String targetServiceId;
        private String targetServiceType;
        private String notifyUrl;

        private Record() {}

        public static Record from(Subscription subscription) {
            Record record = new Record();
            record.subscriptionId = subscription.id();
            record.srcServiceId = subscription.srcServiceId();
            record.targetServiceId = subscription.targetServiceId();
            record.targetServiceType = subscription.targetServiceType();
            record.notifyUrl = subscription.notifyUrl();
            return record;
        }

        public Subscription toSubscription() {
            return Subscription.create().withId(subscriptionId)
                    .withSrcServiceId(srcServiceId)
                    .withTargetServiceId(targetServiceId)
                    .withTargetServiceType(targetServiceType)
                    .withNotifyUrl(notifyUrl);
        }
    }
}
