package db.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String primaryKey) {
        super("record " + primaryKey + " not found");
    }
}
