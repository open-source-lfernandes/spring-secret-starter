package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

/**
 * SecretNotFoundException is a custom exception that is thrown when a secret
 * with the specified key is not found in the secrets provider.
 * It extends Exception and provides a constructor to set the key
 * that was not found.
 */
public class SecretNotFoundException extends Exception {

    public SecretNotFoundException(
        String key
    ) {
        super(String.format("key=%s informed not found", key));
    }
}
