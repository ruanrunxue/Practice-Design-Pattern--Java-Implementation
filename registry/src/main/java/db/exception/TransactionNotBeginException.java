package db.exception;

public class TransactionNotBeginException extends RuntimeException {
    public TransactionNotBeginException(String name) {
        super("Transaction " + name + " not begin yet.");
    }
}
