package com.yrunz.designpattern.db.transaction;

import com.yrunz.designpattern.db.Db;

/**
 * 命令模式
 */

// 执行数据库操作的命令接口
public interface Command {
    // 执行insert、update、delete命令
    void exec();
    // 回滚命令
    void undo();
    // 设置Db
    void setDb(Db db);
}
