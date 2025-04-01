package com.bankTransaction.transaction.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AlreadyExistException extends RuntimeException {
    private String message;
}
