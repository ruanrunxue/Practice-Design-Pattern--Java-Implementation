package com.yrunz.designpattern.db.exception;

public class UpdateOperationNotSupportException extends RuntimeException {
    public UpdateOperationNotSupportException(String tableName) {
        super("table " + tableName + " not support update operation");
    }
}
