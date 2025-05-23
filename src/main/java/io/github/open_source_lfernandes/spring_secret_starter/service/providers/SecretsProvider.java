package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;

import java.util.Optional;

/**
 * SecretsProvider is an interface that defines the contract for secret providers.
 * It allows retrieving secrets by key and provides the origin of the secrets.
 */
public interface SecretsProvider {

    /**
     * Retrieves the origin of the secrets provided by this provider.
     *
     * @return the Origin enum representing the source of the secrets
     */
    Origin getOrigin();

    /**
     * Retrieves a secret by its key.
     *
     * @param key the key of the secret to retrieve
     * @return an Optional containing the SecretDTO if found, or empty if not found
     */
    Optional<SecretDTO> get(String key);
}
