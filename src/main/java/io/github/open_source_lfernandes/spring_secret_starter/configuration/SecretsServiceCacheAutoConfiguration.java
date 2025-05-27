package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderAws;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderCache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.util.StringUtils.hasText;

/**
 * SecretsServiceAwsAutoConfiguration is a Spring configuration class
 * that automatically configures the AWS Secrets Manager client and provider.
 * It uses the provided SecretsProperties to set up the client and provider.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecretsProperties.class)
@ConditionalOnProperty(prefix = "spring.secrets.cache", name = "enabled", havingValue = "true")
public class SecretsServiceCacheAutoConfiguration {

    /**
     * The Secrets properties.
     */
    private final SecretsProperties props;

    /**
     * Post construct method to log the initialization of the AWS Secrets Manager.
     */
    @PostConstruct
    public void postConstruct() {
        log.info("Cache-Secrets-Initiated, enabled={}", props.cache().getEnabled());
    }

    @Bean
    @Scope(scopeName = "singleton")
    public SecretsProviderCache secretsProviderCache() {
        return new SecretsProviderCache();
    }
}
