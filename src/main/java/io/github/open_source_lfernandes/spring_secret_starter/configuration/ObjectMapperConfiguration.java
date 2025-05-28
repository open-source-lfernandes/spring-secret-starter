package io.github.open_source_lfernandes.spring_secret_starter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ObjectMapperConfiguration is a Spring configuration class that provides
 * a default ObjectMapper bean if one is not already defined in the application context.
 * This allows for JSON serialization and deserialization throughout the application.
 */
@Configuration
public class ObjectMapperConfiguration {

    /**
     * Provides a default ObjectMapper bean if one is not already defined.
     * This bean can be used for JSON serialization and deserialization.
     *
     * @return a new instance of ObjectMapper
     */
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
