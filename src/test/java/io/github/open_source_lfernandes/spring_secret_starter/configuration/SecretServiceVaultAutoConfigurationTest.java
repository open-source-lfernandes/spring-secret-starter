package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration(classes = {SecretsServiceVaultAutoConfiguration.class, ObjectMapperConfiguration.class})
@EnableConfigurationProperties(SecretsProperties.class)
class SecretServiceVaultAutoConfigurationTest {


    @Test
    void contextLoads() {
      // tests configuration startup
    }
}
