package io.github.open_source_lfernandes.spring_secret_starter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.UnexpectedInternalErrorException;
import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.AbstractSecretsProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SecretsManagerService is responsible for managing secrets from different providers.
 * It allows retrieving secrets by key and origin.
 */
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecretsManagerService {

    /**
     * The list of secrets providers.
     */
    List<AbstractSecretsProvider> services;

    ObjectMapper objectMapper;

    /**
     * Retrieves a secret by its key from all available providers.
     *
     * @param key the key of the secret to retrieve
     * @return a list of SecretDTO objects containing the secrets
     */
    public List<SecretDTO> get(String key) {
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        return services.stream()
                .map(service -> service.get(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a secret by its key from any available provider.
     * If the secret is found, it returns the first one found.
     *
     * @param key the key of the secret to retrieve
     * @return an Optional containing the SecretDTO object if found, or empty if not found
     */
    public Optional<SecretDTO> getFromAnyProvider(String key) {
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        for (AbstractSecretsProvider service : services) {
            try {
                Optional<SecretDTO> secret = service.get(key);
                if (secret.isPresent()) {
                    return secret;
                }
            } catch (Exception exception) {
                log.warn("Failed to retrieve secret with key '{}' from provider '{}': {}",
                        key, service.getOrigin(), exception.getMessage(), exception);
                // skip to the next provider if an exception occurs
            }
        }

        return Optional.empty();
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

    /**
     * Retrieves a secret by its key and origin, converting the value to the specified type.
     *
     * @param origin the origin of the secret
     * @param key    the key of the secret to retrieve
     * @param type   the class type to convert the secret value to
     * @param <T>    the type of the secret value
     * @return the secret value converted to the specified type
     * @throws SecretNotFoundException if the secret is not found
     */
    public <T> T get(Origin origin, String key, Class<T> type) throws SecretNotFoundException {
        Objects.requireNonNull(origin, Messages.ORIGIN_CANNOT_BE_BULL.getDescription());
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());
        Objects.requireNonNull(type, Messages.TYPE_CANNOT_BE_NULL.getDescription());

        return get(origin, key)
                .map(SecretDTO::value)
                .map(value -> convertJsonToSecretValue(value, type))
                .orElseThrow(() -> new SecretNotFoundException(key));
    }

    /**
     * Retrieves a secret by its key from any available provider, converting the value to the specified type.
     *
     * @param key  the key of the secret to retrieve
     * @param type the class type to convert the secret value to
     * @param <T>  the type of the secret value
     * @return the secret value converted to the specified type
     * @throws SecretNotFoundException if the secret is not found in any provider
     */
    public <T> T getFromAnyProvider(String key, Class<T> type) throws SecretNotFoundException {
        Objects.requireNonNull(key, Messages.KEY_CANNOT_BE_NULL.getDescription());

        for (AbstractSecretsProvider service : services) {
            try {
                Optional<SecretDTO> secret = service.get(key);
                if (secret.isPresent()) {
                    return convertJsonToSecretValue(secret.get().value(), type);
                }
            } catch (Exception exception) {
                log.warn("Failed to retrieve secret with key '{}' from provider '{}': {}",
                        key, service.getOrigin(), exception.getMessage(), exception);
                // skip to the next provider if an exception occurs
            }
        }

        throw new SecretNotFoundException(key);
    }

    /**
     * Converts the secret value to the specified type using the ObjectMapper.
     *
     * @param value the secret value to convert
     * @param type  the class type to convert the secret value to
     * @param <T>   the type of the secret value
     * @return the secret value converted to the specified type
     */
    private <T> T convertJsonToSecretValue(String value, Class<T> type) {
        try {
            return objectMapper.readValue(value, type);
        } catch (Exception exception) {
            log.error("Error parsing secret value from JSON: {}", exception.getMessage(), exception);
            throw new UnexpectedInternalErrorException(Messages.JSON_PARSE_SECRET_VALUE_ERROR.getDescription(), exception);
        }
    }
}
