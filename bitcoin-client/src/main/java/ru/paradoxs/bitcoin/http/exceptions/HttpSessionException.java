package ru.paradoxs.bitcoin.http.exceptions;

public class HttpSessionException extends RuntimeException {

    public HttpSessionException(String message) {
        super(message);
    }

    public HttpSessionException(Throwable ex) {
        super(ex);
    }
}
