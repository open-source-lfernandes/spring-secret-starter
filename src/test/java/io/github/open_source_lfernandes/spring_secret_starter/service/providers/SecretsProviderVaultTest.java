package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.configuration.ObjectMapperConfiguration;
import io.github.open_source_lfernandes.spring_secret_starter.configuration.SecretsServiceVaultAutoConfiguration;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.utils.JsonUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.vault.core.VaultTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.vault.VaultContainer;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration(classes = {
        SecretsServiceVaultAutoConfiguration.class,
        ObjectMapperConfiguration.class
})
@EnableConfigurationProperties(SecretsProperties.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecretsProviderVaultTest {

    @Container
    private static final VaultContainer<?> vaultContainer = new VaultContainer<>("hashicorp/vault:latest")
            .withVaultPort(8200)
            .withVaultToken("testtoken");

    @Autowired
    private SecretsProviderVault secretsProviderVault;

    @Autowired
    private VaultTemplate vaultTemplate;

    private final String key = "password";
    private final String secretValue = "lucas@123";

    @BeforeAll
    void setUpSecret() {
        if (!vaultContainer.isRunning()) {
            vaultContainer.start();
            waitSeconds(10L);
        }
        String path = "secret/data/test";
        vaultTemplate.write(path, Map.of("data", Map.of(key, secretValue)));
        secretsProviderVault = new SecretsProviderVault(new ObjectMapper(), 1, vaultTemplate, path);
    }

    @Test
    public void shouldReturnSecretFromVaultCorrectly() {
        Optional<SecretDTO> optionalSecretDTO = secretsProviderVault.get(key);

        assertNotNull(optionalSecretDTO);
        assertTrue(optionalSecretDTO.isPresent());
        assertEquals(JsonUtils.convertSecretValueToJson(secretValue), optionalSecretDTO.get().value());
    }

    @SneakyThrows
    private static void waitSeconds(long seconds) {
        Thread.sleep(seconds * 1000L);
    }
}
