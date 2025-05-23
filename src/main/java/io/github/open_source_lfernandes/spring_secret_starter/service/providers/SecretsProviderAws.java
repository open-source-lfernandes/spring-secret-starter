package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

/**
 * SecretsProviderAws is an implementation of the SecretsProvider interface
 * that retrieves secrets from AWS Secrets Manager.
 */
@Slf4j
@RequiredArgsConstructor
public class SecretsProviderAws implements SecretsProvider {

    /**
     * The AWS Secrets Manager client.
     */
    private final SecretsManagerClient client;

    @Override
    public Origin getOrigin() {
        return Origin.AWS;
    }

    @Override
    public SecretDTO get(String key) {
        try {
            var request = GetSecretValueRequest.builder()
                .secretId(key)
                .build();

            var response = client.getSecretValue(request);

            return SecretDTO.builder()
                .origin(getOrigin())
                .key(key)
                .value(response.secretString())
                .build();
        } catch (ResourceNotFoundException exception) {
            log.warn("stage=secret-not-found-in-aws, key={}", key);
            return null;
        }
    }
}
