package com.klog.exception;

public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(String message) {
        super("[InternalServerError] " + message);
    }

    public InternalServerErrorException() {
        super("[InternalServerError] " + "internal server error");
    }

}
