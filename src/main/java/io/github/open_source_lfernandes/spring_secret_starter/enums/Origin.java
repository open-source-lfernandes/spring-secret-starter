package io.github.open_source_lfernandes.spring_secret_starter.enums;

/**
 * The Origin enum represents the origin of a secret.
 * It can be either AWS or CUSTOM.
 */
public enum Origin {
    /**
     * AWS indicates that the secret is sourced from AWS Secrets Manager.
     */
    AWS,
    /**
     * CUSTOM indicates that the secret is sourced from a custom provider.
     */
    CUSTOM
}
