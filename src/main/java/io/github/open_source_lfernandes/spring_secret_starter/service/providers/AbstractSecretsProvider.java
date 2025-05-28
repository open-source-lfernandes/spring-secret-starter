package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.UnexpectedInternalErrorException;
import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * SecretsProvider is an abstract class that defines the contract for retrieving secrets
 * from different providers. It provides methods to get the origin of the secrets and
 * to retrieve a secret by its key.
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractSecretsProvider {

    /**
     * The ObjectMapper used for serializing and deserializing secret data.
     */
    private final ObjectMapper objectMapper;

    /**
     * The order of the provider, used to determine the precedence of secret retrieval.
     */
    protected final Integer order;

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

    /**
     * Converts a secret value to its JSON representation.
     *
     * @param secretValue the secret value to convert
     * @return the JSON string representation of the secret value
     * @throws UnexpectedInternalErrorException if there is an error during conversion
     */
    protected String convertSecretValueToJson(Object secretValue) {
        try {
            return objectMapper.writeValueAsString(secretValue);
        } catch (JsonProcessingException e) {
            log.error("Error parsing secret value to JSON: {}", e.getMessage(), e);
            throw new UnexpectedInternalErrorException(Messages.JSON_PARSE_SECRET_VALUE_ERROR.getDescription(), e);
        }
    }
}
