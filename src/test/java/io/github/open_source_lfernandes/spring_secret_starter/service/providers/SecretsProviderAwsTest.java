package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import cloud.localstack.ServiceName;
import cloud.localstack.awssdkv2.TestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = { ServiceName.SECRETSMANAGER })
class SecretsProviderAwsTest {

    private final String key = "key";
    private final String value = "value";
    private SecretsProviderAws secretsProviderAws;

    @BeforeAll
    void setUpSecret() {
        SecretsManagerClient secretsManagerClient = TestUtils.getClientSecretsManagerV2();

        CreateSecretRequest createRequest = CreateSecretRequest.builder()
                .name(key)
                .secretString(value)
                .build();
        secretsManagerClient.createSecret(createRequest);

        secretsProviderAws = new SecretsProviderAws(0, secretsManagerClient);
    }

    @Test
    void shouldReturnSecretFromClientAwsCorrectly() {
        var optionalSecretDTO = Optional.of(
                SecretDTO.builder()
                        .origin(Origin.AWS)
                        .key(key)
                        .value(value)
                        .build()
        );
        assertNotNull(secretsProviderAws.get(key));
        assertEquals(optionalSecretDTO, secretsProviderAws.get(key));
    }

    @Test
    void shouldNotReturnSecretFromClientAws() {
        assertEquals(Optional.empty(), secretsProviderAws.get("wrong-key"));
    }
}
