package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;

/**
 * CannotCastTypeException is a custom exception that is thrown when there is an error
 * while casting a secret value to the expected type.
 * It extends RuntimeException and provides a constructor to set the error message.
 */
public class CannotCastTypeException extends RuntimeException {

    /**
     * Constructs a CannotCastTypeException with a default error message.
     * This constructor is used when the exception is thrown without a specific cause.
     * @param cause the cause of the exception, typically another Throwable that caused this exception to be thrown
     */
    public CannotCastTypeException(Throwable cause) {
        super(Messages.JSON_PARSE_SECRET_VALUE_ERROR.getDescription(), cause);
    }
}
