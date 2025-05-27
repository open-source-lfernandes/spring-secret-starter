package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.exceptions.NoneSecretProviderException;
import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderCache;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;
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
    private final List<SecretsProvider> providers;
    /**
     * The Secrets properties.
     */
    private final SecretsProperties props;
    /**
     * The SecretsProviderCache instance.
     */
    private final SecretsProviderCache secretsProviderCache;

    /**
     * Creates a SecretsManagerService bean if there are any providers available.
     * @return a SecretsManagerService instance
     */
    @Bean
    @Lazy
    public SecretsManagerService secretsManager() {
        if (isNull(providers) || providers.isEmpty())
            throw new NoneSecretProviderException("No Secret Provider Could Be Instantiate! Check your properties/yml file!");

        providers.forEach(provider -> {
        });

        var secretsManagerService = new SecretsManagerService(providers, props.cache().getEnabled(), secretsProviderCache);



        return secretsManagerService;
    }

}
