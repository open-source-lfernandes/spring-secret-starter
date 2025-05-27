package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * SecretsProvider is an abstract class that defines the contract for retrieving secrets
 * from different providers. It provides methods to get the origin of the secrets and
 * to retrieve a secret by its key.
 */
@Getter
@AllArgsConstructor
public abstract class AbstractSecretsProvider {

    /**
     * The order of the provider, used to determine the precedence of secret retrieval.
     */
    protected Integer order;

    /**
     * Retrieves the origin of the secrets provided by this provider.
     *
     * @return the Origin enum representing the source of the secrets
     */
    public abstract Origin getOrigin();

    /**
     * Retrieves a secret by its key.
     *
     * @param key the key of the secret to retrieve
     * @return an Optional containing the SecretDTO if found, or empty if not found
     */
    public abstract Optional<SecretDTO> get(String key);
}
