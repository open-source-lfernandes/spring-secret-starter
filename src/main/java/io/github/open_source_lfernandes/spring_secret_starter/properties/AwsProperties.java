package io.github.open_source_lfernandes.spring_secret_starter.properties;

/**
 * AWS is a record that holds the properties for configuring
 * the AWS Secrets Manager.
 * It contains fields for enabling/disabling the manager and its properties.
 */
public record AwsProperties(SecretsManagerProperties secretsManager) {
}
