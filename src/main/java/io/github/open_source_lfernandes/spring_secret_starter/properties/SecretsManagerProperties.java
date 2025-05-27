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

    /**
     * The AWS region where the Secrets Manager is located.
     */
    String region;
    /**
     * The endpoint URL for the Secrets Manager.
     */
    String endpoint;

    /**
     * Constructor to initialize SecretsManagerProperties with the specified parameters.
     *
     * @param enabled  whether the Secrets Manager is enabled
     * @param order    the order of this property in relation to others
     * @param region   the AWS region where the Secrets Manager is located
     * @param endpoint the endpoint URL for the Secrets Manager
     */
    public SecretsManagerProperties(Boolean enabled, Integer order, String region, String endpoint) {
        super(enabled, order);
        this.region = region;
        this.endpoint = endpoint;
    }
}
