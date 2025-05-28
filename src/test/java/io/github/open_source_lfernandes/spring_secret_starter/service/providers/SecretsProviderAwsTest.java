package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import cloud.localstack.ServiceName;
import cloud.localstack.awssdkv2.TestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;

import java.io.Serializable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = {ServiceName.SECRETSMANAGER})
class SecretsProviderAwsTest {

    private final String keySimpleString = "key";
    private final String valueSimpleString = "value";
    private final String keyObject = "key-object";
    private final Credential valueObject = new Credential("lucas", "123456");
    private SecretsProviderAws secretsProviderAws;

    @BeforeAll
    void setUpSecret() {
        SecretsManagerClient secretsManagerClient = TestUtils.getClientSecretsManagerV2();

        CreateSecretRequest createRequest = CreateSecretRequest.builder()
                .name(keySimpleString)
                .secretString(valueSimpleString)
                .build();
        secretsManagerClient.createSecret(createRequest);

        createRequest = CreateSecretRequest.builder()
                .name(keyObject)
                .secretString(JsonUtils.convertSecretValueToJson(valueObject))
                .build();
        secretsManagerClient.createSecret(createRequest);

        secretsProviderAws = new SecretsProviderAws(new ObjectMapper(), 1, secretsManagerClient);
    }

    @Test
    void shouldReturnSecretFromClientAwsCorrectly() {
        var optionalSimpleSecretDTO = Optional.of(
                SecretDTO.builder()
                        .origin(Origin.AWS)
                        .key(keySimpleString)
                        .value(JsonUtils.convertSecretValueToJson(valueSimpleString))
                        .build()
        );

        var optionalObjectSecretDTO = Optional.of(
                SecretDTO.builder()
                        .origin(Origin.AWS)
                        .key(keyObject)
                        .value(JsonUtils.convertSecretValueToJson(valueObject))
                        .build()
        );

        var secretSimpleStringResponse = secretsProviderAws.get(keySimpleString);
        var secretObjectResponse = secretsProviderAws.get(keyObject);

        assertNotNull(secretSimpleStringResponse);
        assertNotNull(secretObjectResponse);
        assertTrue(secretSimpleStringResponse.isPresent());
        assertTrue(secretObjectResponse.isPresent());

        assertEquals(optionalSimpleSecretDTO.get(), secretSimpleStringResponse.get());
        assertEquals(JsonUtils.convertSecretValueToJson(optionalObjectSecretDTO.get().value()), secretObjectResponse.get().value());
    }

    @Test
    void shouldNotReturnSecretFromClientAws() {
        assertEquals(Optional.empty(), secretsProviderAws.get("wrong-key"));
    }

    @Data
    @AllArgsConstructor
    private static class Credential implements Serializable {
        String username;
        String password;
    }
}
