package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderVault;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

import java.util.Objects;

/**
 * SecretsServiceVaultAutoConfiguration is a Spring configuration class that
 * sets up the VaultTemplate and SecretsProviderVault beans for managing secrets
 * using HashiCorp Vault.
 * It is conditionally enabled based on the properties defined in SecretsProperties.
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecretsProperties.class)
@ConditionalOnProperty(prefix = "spring.secrets.vault", name = "enabled", havingValue = "true")
public class SecretsServiceVaultAutoConfiguration {

    /**
     * The SecretsProperties instance containing the configuration properties
     * for the secrets management system.
     */
    private final SecretsProperties props;
    /**
     * The ObjectMapper instance used for serializing and deserializing JSON data.
     */
    private final ObjectMapper objectMapper;

    /**
     * Creates a VaultTemplate bean if it is not already defined in the application context.
     * The VaultTemplate is configured with the Vault endpoint and authentication token
     * from the SecretsProperties.
     *
     * @return a configured VaultTemplate instance
     */
    @Bean
    @ConditionalOnMissingBean(VaultTemplate.class)
    public VaultTemplate vaultTemplate() {
        var uri = Objects.requireNonNull(props.vault().getUri(), "Vault Uri cannot be null");
        var token = Objects.requireNonNull(props.vault().getToken(), "Vault Token cannot be null");
        return new VaultTemplate(VaultEndpoint.from(uri), new TokenAuthentication(token));
    }

    /**
     * Creates a SecretsProviderVault bean if it is not already defined in the application context.
     * The SecretsProviderVault is configured with the order from the SecretsProperties
     * and the VaultTemplate instance.
     *
     * @return a configured SecretsProviderVault instance
     */
    @Bean
    public SecretsProviderVault secretsVaultProvider(){
        return new SecretsProviderVault(objectMapper, props.vault().getOrder(),
                vaultTemplate(), props.vault().getPath());
    }

}
