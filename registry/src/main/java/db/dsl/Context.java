package db.dsl;

import java.util.List;

// DSL解析器上下文，保存各个表达式解析的中间结果
// 当前只支持基于主键的查询DSL语句
public class Context {
    private String tableName;
    private List<String> columns;
    private Object primaryKeyValue;

    private Context() {}

    public static Context create() {
        return new Context();
    }

    public String tableName() {
        return tableName;
    }

    public List<String> columns() {
        return columns;
    }

    public Object primaryKeyValue() {
        return primaryKeyValue;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setPrimaryKeyValue(Object primaryKeyValue) {
        this.primaryKeyValue = primaryKeyValue;
    }

}
