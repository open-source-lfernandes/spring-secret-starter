package io.github.open_source_lfernandes.spring_secret_starter.service;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProvider;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SecretsManagerService is responsible for managing secrets from different providers.
 * It allows retrieving secrets by key and origin.
 */
@RequiredArgsConstructor
public class SecretsManagerService {

    /**
     * The set of secrets providers.
     */
    private final Set<SecretsProvider> services;

    /**
     * Retrieves a secret by its key from all available providers.
     *
     * @param key the key of the secret to retrieve
     * @return a set of SecretDTO objects containing the secrets
     */
    public Set<SecretDTO> get(String key) {
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        return services.stream()
                .map(service -> service.get(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a secret by its key and origin from the specified provider.
     *
     * @param origin the origin of the secret
     * @param key    the key of the secret to retrieve
     * @return an Optional containing the SecretDTO object if found, or empty if not found
     */
    public Optional<SecretDTO> get(Origin origin, String key) {
        Objects.requireNonNull(origin, Messages.ORIGIN_CANNOT_BE_BULL.getDescription());
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        return services.stream()
                .filter(service -> service.getOrigin().equals(origin))
                .findFirst()
                .flatMap(service -> service.get(key));
    }

    /**
     * Retrieves a secret by its key and origin from the specified provider.
     * If the secret is not found, it throws a SecretNotFoundException.
     *
     * @param origin the origin of the secret
     * @param key    the key of the secret to retrieve
     * @return the SecretDTO object containing the secret
     * @throws SecretNotFoundException if the secret is not found
     */
    public SecretDTO getOrFailure(Origin origin, String key) throws SecretNotFoundException {
        return get(origin, key).orElseThrow(() -> new SecretNotFoundException(key));
    }

}
