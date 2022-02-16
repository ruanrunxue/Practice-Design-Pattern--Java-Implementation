package com.yrunz.designpattern.db.exception;

public class RecordAlreadyExistException extends RuntimeException {
    public RecordAlreadyExistException(String primaryKey) {
        super("record " + primaryKey + " already exist");
    }
}
