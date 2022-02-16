package com.yrunz.designpattern.db.exception;

public class InvalidGrammarException extends RuntimeException {

    public InvalidGrammarException(String expression) {
        super("invalid grammar at '" + expression + "'");
    }
}
