package db.dsl;

public class InvalidGrammarException extends RuntimeException {

    public InvalidGrammarException(String expression) {
        super("invalid grammar at " + expression);
    }
}
