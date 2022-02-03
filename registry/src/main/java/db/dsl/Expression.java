package db.dsl;

/**
 * 解释器模式
 */

// DSL表达式抽象接口，每个词、符号和句子都属于表达式
public interface Expression {
    void interpret(Context context);
}
