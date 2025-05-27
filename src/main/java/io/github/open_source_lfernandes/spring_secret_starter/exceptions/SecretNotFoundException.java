package io.github.open_source_lfernandes.spring_secret_starter.exceptions;

/**
 * SecretNotFoundException is a custom exception that is thrown when a secret
 * with the specified key is not found in the secrets provider.
 * It extends Exception and provides a constructor to set the key
 * that was not found.
 */
public class SecretNotFoundException extends Exception {

    /**
     * Constructs a new SecretNotFoundException with a message indicating
     * that the secret with the specified key was not found.
     *
     * @param key the key of the secret that was not found
     */
    public SecretNotFoundException(
        String key
    ) {
        super(String.format("key=%s informed not found", key));
    }
}
