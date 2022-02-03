package db.dsl;

// MemoryDB DSL语句解释器，DSL固定为select xxx,xxx,xxx from xxx where xxx=xxx 的固定格式
// 例子：select regionId from regionTable where regionId=1
public class CompoundExpression implements Expression {
    private final String dsl;

    private CompoundExpression(String dsl) {
        this.dsl = dsl;
    }

    public static CompoundExpression of(String dsl) {
        return new CompoundExpression(dsl);
    }

    @Override
    public void interpret(Context context) {
        String[] childs = dsl.split(" ");
        if (childs.length != 6) {
            throw new InvalidGrammarException(dsl);
        }
        for (int i = 0; i < childs.length; i++) {
            String expression = childs[i].toLowerCase();
            switch (expression) {
                case "select":
                    SelectExpression.of(childs[++i]).interpret(context);
                    break;
                case "from":
                    FromExpression.of(childs[++i]).interpret(context);
                    break;
                case "where":
                    WhereExpression.of(childs[++i]).interpret(context);
                    break;
                default:
                    throw new InvalidGrammarException(dsl);
            }
        }
    }
}
