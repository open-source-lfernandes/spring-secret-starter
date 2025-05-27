package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.AbstractSecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration(classes = {SecretsManagerServiceAutoConfiguration.class, SecretsServiceAwsAutoConfiguration.class})
@EnableConfigurationProperties(SecretsProperties.class)
class SecretsManagerServiceAutoConfigurationTest {

    @Autowired
    private SecretsManagerService secretsManager;
    @Autowired
    private Set<AbstractSecretsProvider> providers;

    @Test
    void shouldCreateBeanSecretManagerService() {
        Assertions.assertNotNull(secretsManager);
    }

    @Test
    void providersShouldNotBeEmpty() {
        Assertions.assertFalse(providers.isEmpty());
    }
}
