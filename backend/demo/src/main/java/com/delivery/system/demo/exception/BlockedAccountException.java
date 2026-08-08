package com.delivery.system.demo.exception;

public class BlockedAccountException extends RuntimeException {
    public BlockedAccountException(String message) {
        super(message);
    }
}
