package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.annotations.impl.SecretValueBeanPostProcessor;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration class for the SecretValueBeanPostProcessor.
 * This class registers the SecretValueBeanPostProcessor bean in the Spring context.
 * It is responsible for processing fields annotated with @SecretValue and injecting secret values.
 */
@Configuration
public class SecretValueBeanPostProcessorConfiguration {

    /**
     * Creates a SecretValueBeanPostProcessor bean.
     *
     * @param environment the Environment instance used to resolve placeholders in secret keys
     * @param secretsManagerService the SecretsManagerService instance used to retrieve secrets
     * @return a new instance of SecretValueBeanPostProcessor
     */
    @Bean
    public SecretValueBeanPostProcessor secretValueBeanPostProcessor(
            Environment environment,
            SecretsManagerService secretsManagerService) {
        return new SecretValueBeanPostProcessor(environment, secretsManagerService);
    }
}