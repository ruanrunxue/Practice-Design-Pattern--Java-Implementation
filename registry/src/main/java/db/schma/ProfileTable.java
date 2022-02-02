package db.schma;

import db.Table;

public class ProfileTable implements Table<String, Integer> {
    @Override
    public String name() {
        return "ProfileTable";
    }

    @Override
    public Integer query(String key) {
        return 0;
    }

    // 插入表记录
    public void insert(String key, Integer record) {

    }

    // 更新表记录，newRecord为新的记录
    public void update(String key, Integer newRecord) {

    }

    // 删除表记录
    public void delete(String key) {

    }

}
