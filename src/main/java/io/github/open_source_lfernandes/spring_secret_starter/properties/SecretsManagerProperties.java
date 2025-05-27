package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * SecretsManagerProperties is a class that holds the properties for configuring
 * the AWS Secrets Manager.
 * <br>
 * It contains fields for enabling/disabling the manager, region, endpoint, list of secrets and enabling/disabling store secrets in cache on startup.
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecretsManagerProperties extends AbstractSecretProperties {
    String region;
    String endpoint;

    public SecretsManagerProperties(Boolean enabled, List<String> keys, Boolean cachingOnStartup,
                                    String region, String endpoint) {
        super(enabled, keys, cachingOnStartup);
        this.region = region;
        this.endpoint = endpoint;
    }
}
