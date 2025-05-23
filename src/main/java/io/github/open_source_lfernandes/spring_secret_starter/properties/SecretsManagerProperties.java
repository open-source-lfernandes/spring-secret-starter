package io.github.open_source_lfernandes.spring_secret_starter.properties;

/**
 * SecretsManagerProperties is a record that holds the properties for
 * configuring the AWS Secrets Manager.
 * It contains fields for enabling/disabling the manager, the region, and the endpoint.
 */
public record SecretsManagerProperties(Boolean enabled, String region, String endpoint) {

}
