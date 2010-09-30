package ru.paradoxs.bitcoin.client.exceptions;

public class BitcoinClientException extends RuntimeException {

    public BitcoinClientException(String message) {
        super(message);
    }

    public BitcoinClientException(Throwable ex) {
        super(ex);
    }
}
