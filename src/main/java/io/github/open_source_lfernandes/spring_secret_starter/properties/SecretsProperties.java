package io.github.open_source_lfernandes.spring_secret_starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SecretsProperties is a record that holds the properties for configuring
 * the secrets management system.
 * It contains fields for AWS configuration.
 */
@ConfigurationProperties(prefix = "spring.secrets")
public record SecretsProperties(
        AwsProperties aws,
        CacheProperties cache
) {

}