package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

/**
 * OriginRequestedNotProvidedException is a custom exception that is thrown when
 * a requested origin for a secret is not provided.
 * It extends RuntimeException and provides a constructor to set the error message.
 */
public class OriginRequestedNotProvidedException extends RuntimeException {
    /**
     * Constructs a new OriginRequestedNotProvidedException with the specified detail message.
     *
     * @param message the detail message
     */
    public OriginRequestedNotProvidedException(String message) {
        super(message);
    }
}
