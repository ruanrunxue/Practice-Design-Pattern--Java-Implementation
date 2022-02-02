package db.schma;

import db.Table;
import domain.ServiceProfile;

import java.util.HashMap;
import java.util.Map;

public class ProfileTable implements Table<String, ServiceProfile> {
    private final Map<String, ServiceProfile> profiles;

    private ProfileTable() {
        profiles = new HashMap<>();
    }

    public static ProfileTable empty() {
        return new ProfileTable();
    }

    @Override
    public String name() {
        return "ProfileTable";
    }

    @Override
    public ServiceProfile query(String key) {
        return null;
    }

    // 插入表记录
    @Override
    public void insert(String key, ServiceProfile record) {

    }

    // 更新表记录，newRecord为新的记录
    @Override
    public void update(String key, ServiceProfile newRecord) {

    }

    // 删除表记录
    public void delete(String key) {

    }

}
