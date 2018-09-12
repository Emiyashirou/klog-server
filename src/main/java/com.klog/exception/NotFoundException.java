package com.klog.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super("[NotFound] " + message);
    }

}
