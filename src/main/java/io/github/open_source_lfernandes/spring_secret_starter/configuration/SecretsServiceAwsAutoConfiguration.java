package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import io.github.open_source_lfernandes.spring_secret_starter.properties.SecretsProperties;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderAws;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.util.StringUtils.hasText;

/**
 * SecretsServiceAwsAutoConfiguration is a Spring configuration class
 * that automatically configures the AWS Secrets Manager client and provider.
 * It uses the provided SecretsProperties to set up the client and provider.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecretsProperties.class)
@ConditionalOnProperty(prefix = "spring.secrets.aws.secrets-manager", name = "enabled", havingValue = "true")
public class SecretsServiceAwsAutoConfiguration {

    /**
     * The AWS Secrets Manager properties.
     */
    private final SecretsProperties props;

    /**
     * Post construct method to log the initialization of the AWS Secrets Manager.
     */
    @PostConstruct
    public void postConstruct() {
        log.info("Secret-Manager-AWS-Initiated, enabled={}, region={}, endpoint={}", props.aws().secretsManager().enabled(),
                props.aws().secretsManager().region(), props.aws().secretsManager().endpoint());
    }

    /**
     * Creates a SecretsProviderAws bean if the AWS Secrets Manager is enabled.
     * @return a SecretsProviderAws instance
     * @throws URISyntaxException if the endpoint URI is invalid
     */
    @Bean
    public SecretsProviderAws secretsProviderAws() throws URISyntaxException {
        return new SecretsProviderAws(secretsManagerClient());
    }

    /**
     * Creates a SecretsManagerClient bean if it is not already defined.
     * @return a SecretsManagerClient instance
     * @throws URISyntaxException if the endpoint URI is invalid
     */
    @Bean
    @ConditionalOnMissingBean(SecretsManagerClient.class)
    public SecretsManagerClient secretsManagerClient() throws URISyntaxException {
        var builder = SecretsManagerClient.builder()
                .credentialsProvider(defaultCredentialsProvider());
        if (hasText(props.aws().secretsManager().endpoint())) {
            builder.endpointOverride(new URI(props.aws().secretsManager().endpoint()));
        }
        if (hasText(props.aws().secretsManager().region())) {
            builder.region(Region.of(props.aws().secretsManager().region()));
        }
        return builder.build();
    }

    /**
     * Creates a DefaultCredentialsProvider bean if it is not already defined.
     * @return a DefaultCredentialsProvider instance
     */
    @Bean
    @ConditionalOnMissingBean(DefaultCredentialsProvider.class)
    public DefaultCredentialsProvider defaultCredentialsProvider() {
        return DefaultCredentialsProvider.builder().build();
    }
}
