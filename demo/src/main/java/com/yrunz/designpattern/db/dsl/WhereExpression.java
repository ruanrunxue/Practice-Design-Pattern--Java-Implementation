package com.yrunz.designpattern.db.dsl;

import com.yrunz.designpattern.db.exception.InvalidGrammarException;

// where语句解析逻辑，where关键字后面跟的为xxx=xxx，当前只支持主键的条件过滤
public class WhereExpression implements Expression {
    private final String condition;

    private WhereExpression(String condition) {
        this.condition = condition;
    }

    public static WhereExpression of(String condition) {
        return new WhereExpression(condition);
    }

    @Override
    public void interpret(Context context) {
        String[] elems = condition.split("=");
        if (elems.length != 2) {
            throw new InvalidGrammarException(condition);
        }
        context.setPrimaryKeyValue(elems[1]);
    }

}
