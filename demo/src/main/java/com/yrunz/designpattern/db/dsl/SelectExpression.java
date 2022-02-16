package com.yrunz.designpattern.db.dsl;

import com.yrunz.designpattern.db.exception.InvalidGrammarException;

import java.util.Arrays;

// select语句解析逻辑，Select关键字后面跟的为column，以,分割，比如Select regionId,regionName
public class SelectExpression implements Expression {
    private final String columns;

    private SelectExpression(String columns) {
        this.columns = columns;
    }

    public static SelectExpression of(String dsl) {
        return new SelectExpression(dsl);
    }

    @Override
    public void interpret(Context context) {
        String[] cols = columns.split(",");
        if (cols.length == 0) {
            throw new InvalidGrammarException(columns);
        }
        context.setColumns(Arrays.asList(cols));
    }
}
