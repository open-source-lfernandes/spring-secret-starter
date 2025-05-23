package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import cloud.localstack.ServiceName;
import cloud.localstack.awssdkv2.TestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;

import static org.junit.jupiter.api.Assertions.*;

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

        secretsProviderAws = new SecretsProviderAws(secretsManagerClient);
    }

    @Test
    void shouldReturnSecretFromClientAwsCorrectly() {
        assertNotNull(secretsProviderAws.get(key));
        assertEquals(value, secretsProviderAws.get(key).value());
    }

    @Test
    void shouldNotReturnSecretFromClientAws() {
        assertNull(secretsProviderAws.get("wrong-key"));
    }
}
