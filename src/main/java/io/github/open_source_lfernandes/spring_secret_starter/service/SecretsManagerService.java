package io.github.open_source_lfernandes.spring_secret_starter.service;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderCache;
import lombok.RequiredArgsConstructor;

import java.util.List;
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
    private final List<SecretsProvider> services;
    /**
     * Indicates whether caching is enabled for the secrets.
     */
    private final boolean isCachingEnabled;

    /**
     * The SecretsProviderCache instance used for caching secrets.
     */
    private final SecretsProviderCache secretsProviderCache;

    /**
     * Retrieves all secrets from all providers.
     * If caching is enabled, it updates the cache with the retrieved secrets.
     *
     * @return a List of SecretDTO containing all secrets
     */
    public List<SecretDTO> get(String key) {
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        return services.stream()
                .map(service -> service.get(key))
                .filter(Optional::isPresent)
                .map(optionalSecretDTO -> {
                    if (isCachingEnabled) {
                        optionalSecretDTO.ifPresent(this::updateCache);
                    }
                    return optionalSecretDTO.get();
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a secret by its key and origin from the specified provider.
     * If caching is enabled, it updates the cache with the retrieved secret.
     *
     * @param origin the origin of the secret
     * @param key    the key of the secret to retrieve
     * @return an Optional containing the SecretDTO if found, or empty if not found
     */
    public Optional<SecretDTO> get(Origin origin, String key) {
        Objects.requireNonNull(origin, Messages.ORIGIN_CANNOT_BE_BULL.getDescription());
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        return services.stream()
                .filter(service -> canGetFromCache(key)
                        ? service.getOrigin().equals(Origin.CACHE)
                        : service.getOrigin().equals(origin))
                .findFirst()
                .flatMap(service ->
                    service.get(key).map(secretDTO -> {
                        if (isCachingEnabled) {
                            updateCache(secretDTO);
                        }
                        return Optional.of(secretDTO);
                    }).orElseGet(Optional::empty)
                );
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

    private void updateCache(SecretDTO secretDTO) {
        secretsProviderCache.put(secretDTO);
    }

    private boolean canGetFromCache(String key) {
        return isCachingEnabled && secretsProviderCache.get(key).isPresent();
    }
}
