package com.yrunz.designpattern.db.transaction;

/**
 * 命令模式
 */

// 执行数据库操作的命令接口
public interface Command<PrimaryKey, Record> {
    // 执行insert、update、delete命令
    void exec();
    // 回滚命令
    void undo();
}
