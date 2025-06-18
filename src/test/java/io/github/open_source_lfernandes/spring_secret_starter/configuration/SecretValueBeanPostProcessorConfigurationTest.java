package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.annotations.SecretValue;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.AbstractSecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.utils.faker.Credential;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        ObjectMapperConfiguration.class,
        SecretValueBeanPostProcessorConfigurationTest.CustomSecretsProviderConfiguration.class
})
@EnableConfigurationProperties(SecretsProperties.class)
class SecretValueBeanPostProcessorConfigurationTest {

    static final String CUSTOM_KEY = "secret-key";
    static final String CUSTOM_VALUE = "value-from-custom-provider";
    static final String CUSTOM_OBJECT_KEY = "secret-object-key";
    static final Credential CUSTOM_OBJECT_VALUE_CREDENTIAL = new Credential("customUser", "customPassword");

    @SecretValue("${example.secret-key}")
    private String secretValue;
    @SecretValue(value = "${example.credential}", type = Credential.class)
    private Credential secretCredentialValue;

    @Test
    void contextLoads() {
        // tests configuration startup
        assertEquals(CUSTOM_VALUE, secretValue);
        assertEquals(CUSTOM_OBJECT_VALUE_CREDENTIAL, secretCredentialValue);
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
                        .value(CUSTOM_VALUE)
                        .build()
                );
            }
            return Optional.empty();
        }

        @Override
        public <T> T get(String key, Class<T> type) throws SecretNotFoundException {
            if (CUSTOM_OBJECT_KEY.equals(key)) {
                return (T) CUSTOM_OBJECT_VALUE_CREDENTIAL;
            }
            throw new SecretNotFoundException(key);
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
