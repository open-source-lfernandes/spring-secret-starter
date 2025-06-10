package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.annotations.impl.SecretValueBeanPostProcessor;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SecretValueBeanPostProcessorConfiguration {

    @Bean
    public SecretValueBeanPostProcessor secretValueBeanPostProcessor(
            Environment environment,
            SecretsManagerService secretsManagerService) {
        return new SecretValueBeanPostProcessor(environment, secretsManagerService);
    }
}