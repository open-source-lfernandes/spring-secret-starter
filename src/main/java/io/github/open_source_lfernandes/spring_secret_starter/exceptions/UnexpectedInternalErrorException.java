package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

public class UnexpectedInternalErrorException extends RuntimeException {

    public UnexpectedInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
