package com.bankTransaction.transaction.exception;

public class OperationExpiredException extends RuntimeException {
    public OperationExpiredException(String message) {
        super(message);
    }
}
