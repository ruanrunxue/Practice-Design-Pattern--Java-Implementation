package com.yrunz.designpattern.monitor.output;

import com.yrunz.designpattern.db.MemoryDb;
import com.yrunz.designpattern.db.exception.TableAlreadyExistException;
import com.yrunz.designpattern.db.schema.MonitorEventTable;
import com.yrunz.designpattern.domain.MonitorEvent;
import com.yrunz.designpattern.monitor.plugin.Config;
import com.yrunz.designpattern.monitor.plugin.Event;

// 将MonitorEvent存储到MemoryDb上
public class MemoryDbOutput implements OutputPlugin {
    private String tableName;

    @Override
    public void output(Event event) {
        if (!(event.payload() instanceof MonitorEvent)) {
            return;
        }
        MonitorEvent monitorEvent = (MonitorEvent) event.payload();
        MonitorEventTable table = (MonitorEventTable) MemoryDb.instance().tableOf(tableName);
        table.insert(monitorEvent.id(), monitorEvent);
    }

    @Override
    public void setContext(Config.Context context) {
        this.tableName = context.getString("tableName");
    }

    @Override
    public void install() {
        try {
            MemoryDb.instance().createTable(MonitorEventTable.of(tableName));
        } catch (TableAlreadyExistException e) {
            // 如果已经创建过，则不再创建
        }
    }

    @Override
    public void uninstall() {

    }
}
