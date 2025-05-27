package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * SecretsManagerProperties is a class that holds the properties for configuring
 * the AWS Secrets Manager. It extends AbstractProperties to include common fields
 * such as enabled status and order.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecretsManagerProperties extends AbstractProperties {

    String region;
    String endpoint;

    public SecretsManagerProperties(Boolean enabled, Integer order, String region, String endpoint) {
        super(enabled, order);
        this.region = region;
        this.endpoint = endpoint;
    }
}
