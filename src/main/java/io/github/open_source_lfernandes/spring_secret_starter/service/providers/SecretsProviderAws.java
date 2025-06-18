package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

import java.util.Optional;

/**
 * SecretsProviderAws is an implementation of the SecretsProvider interface
 * that retrieves secrets from AWS Secrets Manager.
 */
@Slf4j
public class SecretsProviderAws extends AbstractSecretsProvider {

    /**
     * The AWS Secrets Manager client.
     */
    private final SecretsManagerClient client;

    /**
     * Constructs a SecretsProviderAws with the specified order and AWS Secrets Manager client.
     *
     * @param objectMapper the ObjectMapper for JSON serialization/deserialization
     * @param order        the order of the provider, used to determine the precedence of secret retrieval
     * @param client       the AWS Secrets Manager client for interacting with AWS Secrets Manager
     */
    public SecretsProviderAws(ObjectMapper objectMapper, Integer order, SecretsManagerClient client) {
        super(objectMapper, order);
        this.client = client;
    }

    @Override
    public Origin getOrigin() {
        return Origin.AWS;
    }

    @Override
    public Optional<SecretDTO> get(String key) {
        try {
            var request = GetSecretValueRequest.builder()
                    .secretId(key)
                    .build();

            var response = client.getSecretValue(request);

            return Optional.of(
                    SecretDTO.builder()
                            .origin(getOrigin())
                            .key(key)
                            .value(response.secretString())
                            .build()
            );
        } catch (ResourceNotFoundException exception) {
            logError(key);
            return Optional.empty();
        }
    }

    @Override
    public <T> T get(String key, Class<T> type) throws SecretNotFoundException {
        GetSecretValueResponse response;
        try {
            var request = GetSecretValueRequest.builder()
                    .secretId(key)
                    .build();

            response = client.getSecretValue(request);

        } catch (ResourceNotFoundException exception) {
            logError(key);
            throw new SecretNotFoundException(Messages.SECRET_NOT_FOUND.getDescription());
        }
        return convertJsonStringToTypeInstance(response.secretString(), type);
    }

    /**
     * Logs an error message indicating that a secret with the specified key was not found in AWS Secrets Manager.
     *
     * @param key the key of the secret that was not found
     */
    private void logError(String key) {
        log.error("stage=secret-not-found-in-aws, key={}", key);
    }
}
