package com.restaurant.management.exception;

public class BlockedAccountException extends RuntimeException {
    public BlockedAccountException(String message) {
        super(message);
    }
}
