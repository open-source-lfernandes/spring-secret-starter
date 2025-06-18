package io.github.open_source_lfernandes.spring_secret_starter.annotations;

import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;

import java.lang.annotation.*;

/**
 * SecretValue annotation is used to inject secret values into fields or parameters.
 * It allows specifying the key of the secret and the origin from which it should be retrieved.
 * The origin can be AWS, VAULT, CUSTOM, or ANY.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecretValue {
    /**
     * The key of the secret to be injected.
     * It can be a simple key or a placeholder expression.
     *
     * @return the key of the secret
     */
    String value();
    /**
     * The origin of the secret.
     * It can be AWS, VAULT, CUSTOM, or ANY.
     * Default is ANY, which means it will search in all available providers.
     *
     * @return the origin of the secret
     */
    Origin origin() default Origin.ANY;

    /**
     * The type to which the secret value should be converted.
     * Default is String.class, meaning the secret will be injected as a String.
     *
     * @return the Class type to convert the secret value to
     */
    Class<?> type() default String.class;
}
