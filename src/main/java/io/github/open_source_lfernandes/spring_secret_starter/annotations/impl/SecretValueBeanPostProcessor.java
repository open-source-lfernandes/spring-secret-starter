package io.github.open_source_lfernandes.spring_secret_starter.annotations.impl;

import io.github.open_source_lfernandes.spring_secret_starter.annotations.SecretValue;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;

public class SecretValueBeanPostProcessor implements BeanPostProcessor {

    private final Environment environment;
    private final SecretsManagerService secretService;

    public SecretValueBeanPostProcessor(Environment environment, SecretsManagerService secretService) {
        this.environment = environment;
        this.secretService = secretService;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        processFields(bean, clazz);
        return bean;
    }

    private void processFields(Object bean, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            SecretValue annotation = field.getAnnotation(SecretValue.class);
            if (annotation != null) {
                String key = resolveKey(annotation.value());
                String secret = getSecretFromProviders(key, annotation.origin());
                injectValue(bean, field, secret);
            }
        }
    }

    private String getSecretFromProviders(String key, Origin origin) {
        if (origin == Origin.ANY) {
            return secretService.getFromAnyProvider(key)
                    .map(SecretDTO::value)
                    .orElseThrow(() -> new RuntimeException("Secret not found for key: " + key));
        }
        return secretService.get(origin, key)
                .map(SecretDTO::value)
                .orElseThrow(() -> new RuntimeException("Secret not found for key: " + key + " and origin: " + origin));
    }

    private String resolveKey(String expression) {
        return environment.resolveRequiredPlaceholders(expression);
    }

    private void injectValue(Object bean, Field field, String value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject secret into field " + field.getName(), e);
        }
    }

}
