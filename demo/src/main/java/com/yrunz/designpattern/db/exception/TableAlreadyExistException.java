package com.yrunz.designpattern.db.exception;

public class TableAlreadyExistException extends RuntimeException {
    public TableAlreadyExistException(String tableName) {
        super("table " + tableName + " already exist");
    }
}
