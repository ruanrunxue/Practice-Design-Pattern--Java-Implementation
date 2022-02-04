package com.yrunz.designpattern.db.dsl;

import com.yrunz.designpattern.db.exception.InvalidGrammarException;

// from语句解析逻辑，From关键字后面跟的为表名，比如From RegionTable1
public class FromExpression implements Expression {
    private final String tableName;

    private FromExpression(String tableName) {
        this.tableName = tableName;
    }

    public static FromExpression of(String tableName) {
        return new FromExpression(tableName);
    }

    @Override
    public void interpret(Context context) {
        if (tableName.equalsIgnoreCase("")) {
            throw new InvalidGrammarException("table empty");
        }
        context.setTableName(tableName);
    }
}
