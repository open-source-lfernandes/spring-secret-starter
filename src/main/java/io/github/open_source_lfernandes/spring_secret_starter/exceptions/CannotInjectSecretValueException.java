package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;

/**
 * CannotInjectSecretValueException is a custom exception that is thrown when there is an error
 * while injecting a secret value into a field or constructor.
 * It extends RuntimeException and provides a constructor to set the error message.
 */
public class CannotInjectSecretValueException extends RuntimeException {
    /**
     * Constructs a CannotInjectSecretValueException with a default error message.
     * This constructor is used when the exception is thrown without a specific cause.
     *
     * @param cause the cause of the exception, typically another Throwable that caused this exception to be thrown
     */
    public CannotInjectSecretValueException(Throwable cause) {
        super(Messages.CANNOT_INJECT_SECRET_VALUE.getDescription(), cause);
    }
}
