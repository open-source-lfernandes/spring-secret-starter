package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * SecretsVaultProperties is a class that holds the properties for configuring
 * the Secrets Vault. It extends AbstractProperties to include common fields
 * such as enabled status and order.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecretsVaultProperties extends AbstractProperties {

    /**
     * The URI of the Secrets Vault.
     */
    String uri;
    /**
     * The path in the Secrets Vault where secrets are stored.
     */
    String path;
    /**
     * The token used for authentication with the Secrets Vault.
     */
    String token;

    /**
     * Constructor to initialize SecretsVaultProperties with the specified parameters.
     *
     * @param enabled whether the Secrets Vault is enabled
     * @param order   the order of this property in relation to others
     * @param uri     the URI of the Secrets Vault
     * @param path    the path in the Secrets Vault where secrets are stored
     * @param token   the token used for authentication with the Secrets Vault
     */
    public SecretsVaultProperties(Boolean enabled, Integer order, String uri, String path, String token) {
        super(enabled, order);
        this.uri = uri;
        this.path = path;
        this.token = token;
    }
}
