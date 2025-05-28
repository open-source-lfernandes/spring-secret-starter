package io.github.open_source_lfernandes.spring_secret_starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SecretsProperties is a record that holds the properties for configuring
 * the secrets management system.
 * It contains fields for AWS configuration.
 */
@ConfigurationProperties(prefix = "spring.secrets")
public record SecretsProperties(
        AWS aws,
        SecretsVaultProperties vault
) {
    /**
     * AWS is a record that holds the properties for configuring
     * the AWS Secrets Manager.
     * It contains fields for enabling/disabling the manager and its properties.
     */
    public record AWS (SecretsManagerProperties secretsManager){}
}