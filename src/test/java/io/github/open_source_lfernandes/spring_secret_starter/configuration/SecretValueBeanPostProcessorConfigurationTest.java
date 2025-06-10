package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.annotations.SecretValue;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.AbstractSecretsProvider;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@ContextConfiguration(classes = {
        SecretsManagerServiceAutoConfiguration.class,
        SecretValueBeanPostProcessorConfiguration.class,
        SecretsServiceAwsAutoConfiguration.class,
        ObjectMapperConfiguration.class,
        SecretValueBeanPostProcessorConfigurationTest.CustomSecretsProviderConfiguration.class
})
@EnableConfigurationProperties(SecretsProperties.class)
class SecretValueBeanPostProcessorConfigurationTest {

    static final String CUSTOM_KEY = "secret-key";

    @SecretValue("${example.secret-key}")
    private String secretValue;

    @Test
    void contextLoads() {
        // tests configuration startup
        assertEquals("value-from-custom-provider", secretValue);
    }

    // Custom Secrets Provider for testing
    static class CustomSecretsProvider extends AbstractSecretsProvider {
        public CustomSecretsProvider(Integer order) {
            super(new ObjectMapper(), order);
        }

        @Override
        public Origin getOrigin() {
            return Origin.CUSTOM;
        }

        @Override
        public Optional<SecretDTO> get(String key) {
            if (CUSTOM_KEY.equals(key)) {
                return Optional.of(SecretDTO.builder()
                        .origin(Origin.CUSTOM)
                        .key(key)
                        .value("value-from-custom-provider")
                        .build()
                );
            }
            return Optional.empty();
        }
    }

    // Configuration class to provide the custom secrets provider
    @Configuration
    static class CustomSecretsProviderConfiguration {

        @Bean
        public CustomSecretsProvider customSecretsProvider() {
            return new CustomSecretsProvider(1);
        }
    }
}
