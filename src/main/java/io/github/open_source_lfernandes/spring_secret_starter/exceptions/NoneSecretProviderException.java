package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

/**
 * NoneSecretProviderException is a custom exception that is thrown when
 * there is no secret provider available.
 * It extends RuntimeException and provides a constructor to set the error message.
 */
public class NoneSecretProviderException extends RuntimeException {
    public NoneSecretProviderException(String message) {
        super(message);
    }
}
