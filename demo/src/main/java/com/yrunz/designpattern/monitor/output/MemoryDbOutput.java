package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.db.Db;
import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.cache.CacheDbProxy;
import com.yrunz.designpattern.db.exception.TableAlreadyExistException;
import com.yrunz.designpattern.monitor.model.schema.MonitorEventTable;
import com.yrunz.designpattern.monitor.model.MonitorEvent;
import com.yrunz.designpattern.monitor.config.Config;
import com.yrunz.designpattern.monitor.plugin.Event;

// 将MonitorEvent存储到MemoryDb上
public class MemoryDbOutput implements OutputPlugin {
    private String tableName;
    private final Db db;

    public MemoryDbOutput() {
        db = CacheDbProxy.of(MemoryDb.instance());
    }

    @Override
    public void output(Event event) {
        if (!(event.payload() instanceof MonitorEvent)) {
            return;
        }
        MonitorEvent monitorEvent = (MonitorEvent) event.payload();
        db.insert(tableName, monitorEvent.id(), monitorEvent);
    }

    @Override
    public void setContext(Config.Context context) {
        this.tableName = context.getString("tableName");
    }

    @Override
    public void install() {
        try {
            db.createTable(MonitorEventTable.of(tableName));
        } catch (TableAlreadyExistException e) {
            // 如果已经创建过，则不再创建
        }
    }

    @Override
    public void uninstall() {

    }
}
