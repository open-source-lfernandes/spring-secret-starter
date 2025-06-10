package io.github.open_source_lfernandes.spring_secret_starter.annotations.impl;

import io.github.open_source_lfernandes.spring_secret_starter.annotations.SecretValue;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;

/**
 * SecretValueBeanPostProcessor is a Spring BeanPostProcessor that processes fields annotated with
 * SecretValue.
 * <br>
 * It retrieves the secret value from the SecretsManagerService and injects it into the field.
 *
 * @see SecretValue
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecretValueBeanPostProcessor implements BeanPostProcessor {

    /**
     * The Environment instance used to resolve placeholders in the secret key.
     */
    Environment environment;
    /**
     * The SecretsManagerService instance used to retrieve secrets from various providers.
     */
    SecretsManagerService secretService;

    /**
     * Post-processes the bean before initialization. It scans the fields of the bean for the @SecretValue annotation
     * and injects the corresponding secret value into the field.
     *
     * @param bean     the bean instance being processed
     * @param beanName the name of the bean
     * @return the processed bean with secret values injected
     * @throws BeansException if any error occurs during processing
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        processFields(bean, clazz);
        return bean;
    }

    /**
     * Post-processes the bean after initialization. This implementation does not perform any additional processing.
     *
     * @param bean     the bean instance being processed
     * @param clazz    the class type of the bean
     * @throws BeansException if any error occurs during processing
     */
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

    /**
     * Retrieves the secret value from the SecretsManagerService based on the provided key and origin.
     * If the origin is ANY, it retrieves the secret from any available provider.
     *
     * @param key    the key of the secret to retrieve
     * @param origin the origin of the secret
     * @return the secret value as a String
     */
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

    /**
     * Resolves the key by replacing any placeholders in the expression using the Environment.
     *
     * @param expression the expression containing the key, possibly with placeholders
     * @return the resolved key as a String
     */
    private String resolveKey(String expression) {
        return environment.resolveRequiredPlaceholders(expression);
    }

    /**
     * Injects the secret value into the specified field of the bean.
     *
     * @param bean  the bean instance
     * @param field the field to inject the secret value into
     * @param value the secret value to inject
     */
    private void injectValue(Object bean, Field field, String value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject secret into field " + field.getName(), e);
        }
    }

}
