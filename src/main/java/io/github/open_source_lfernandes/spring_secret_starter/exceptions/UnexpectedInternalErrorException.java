package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

/**
 * UnexpectedInternalErrorException is a custom runtime exception that is thrown
 * when an unexpected internal error occurs in the application.
 * It extends RuntimeException and provides a constructor to set the error message
 * and the cause of the exception.
 */
public class UnexpectedInternalErrorException extends RuntimeException {

    /**
     * Constructs a new UnexpectedInternalErrorException with the specified detail message.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public UnexpectedInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
