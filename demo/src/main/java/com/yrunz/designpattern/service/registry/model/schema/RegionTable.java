package com.yrunz.designpattern.service.registry.model.schema;

import com.yrunz.designpattern.db.Table;
import com.yrunz.designpattern.db.TableIterator;
import com.yrunz.designpattern.db.TableVisitor;
import com.yrunz.designpattern.db.exception.RecordAlreadyExistException;
import com.yrunz.designpattern.db.exception.RecordNotFoundException;
import com.yrunz.designpattern.db.iterator.SortedIterator;
import com.yrunz.designpattern.db.PrimaryKey;
import com.yrunz.designpattern.service.registry.model.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 享元模式
 * 享元模式摒弃了在每个对象中保存所有数据的方式， 通过共享多个对象所共有的相同状态， 让你能在有限的内存容量中载入更多对象
 * 当我们决定对一个重型对象采用享元模式进行优化时，首先需要将该重型对象的属性划分为两类，能够共享的和不能共享的。前者我们
 * 称为内部状态（intrinsic state），存储在享元中，不随享元所处上下文的变化而变化；后者称为外部状态（extrinsic state），
 * 它的值取决于享元所处的上下文，因此不能共享。
 * 实现享元模式的关键点：
 * 1、根据业务的上下文，定义享元对象（本例子中为RegionTable.Record）
 * 2、其他对象通过Id/引用共享享元对象（本例子中为ServiceProfile.Record通过regionId共享RegionTable.Record）
 */

// Region表定义
public class RegionTable implements Table<String, Region> {
    private final String name;
    // 使用HashMap存储表记录，key为Region.Id, value为RegionTable.Record
    private final Map<String, Record> records;

    private RegionTable(String name) {
        this.name = name;
        this.records = new HashMap<>();
    }

    public static RegionTable of(String name) {
        return new RegionTable(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Optional<Region> query(String regionId) {
        if (!records.containsKey(regionId)) {
            return Optional.empty();
        }
        RegionTable.Record record = records.get(regionId);
        return Optional.of(record.toRegion());
    }

    // 插入表记录
    @Override
    public void insert(String regionId, Region region) {
        if (records.containsKey(regionId)) {
            throw new RecordAlreadyExistException(regionId);
        }
        records.put(regionId, Record.from(region));
    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(String regionId, Region newRegion) {
        if (!records.containsKey(regionId)) {
            throw new RecordNotFoundException(regionId);
        }
        records.replace(regionId, Record.from(newRegion));
    }

    // 删除表记录
    @Override
    public void delete(String regionId) {
        if (!records.containsKey(regionId)) {
            throw new RecordNotFoundException(regionId);
        }
        records.remove(regionId);
    }

    @Override
    public TableIterator<Region> iterator() {
        List<Region> regions = records.values()
                .stream()
                .map(Record::toRegion)
                .collect(Collectors.toList());
        return new SortedIterator<>(regions);
    }

    @Override
    public List<Region> accept(TableVisitor<Region> visitor) {
        return visitor.visit(this);
    }

    // 享元模式 关键点1：根据业务的上下文，定义享元对象，其他对象通过regionId共享RegionTable.Record
    // Region表结构定义，为享元对象，由ServiceProfileTable.Record通过regionId共享
    private static class Record {
        @PrimaryKey(fieldName = "regionId")
        private String regionId;
        private String regionName;
        private String regionCountry;

        private Record() {}

        public static Record from(Region region) {
            Record record = new Record();
            record.regionId = region.id();
            record.regionName = region.name();
            record.regionCountry = region.country();
            return record;
        }

        public Region toRegion() {
            return Region.of(regionId).withName(regionName).withCountry(regionCountry);
        }
    }

}
