package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.exceptions.NoneSecretProviderException;
import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static java.util.Objects.isNull;

/**
 * SecretsManagerServiceAutoConfiguration is a Spring configuration class
 * that automatically configures the SecretsManagerService bean.
 * It uses the provided SecretsProvider implementations to manage secrets.
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SecretsProperties.class)
public class SecretsManagerServiceAutoConfiguration {

    /**
     * The set of secrets providers.
     */
    private final Set<SecretsProvider> providers;

    /**
     * Creates a SecretsManagerService bean if there are any providers available.
     * @return a SecretsManagerService instance
     */
    @Bean
    public SecretsManagerService secretsManager() {
        if (isNull(providers) || providers.isEmpty())
            throw new NoneSecretProviderException("No Secret Provider Could Be Instantiate! Check your properties/yml file!");
        return new SecretsManagerService(providers);
    }

}
