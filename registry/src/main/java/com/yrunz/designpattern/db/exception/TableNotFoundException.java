package com.yrunz.designpattern.db.exception;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String tableName) {
        super("table " + tableName + " not found");
    }
}
