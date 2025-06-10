package io.github.open_source_lfernandes.spring_secret_starter.annotations;

import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecretValue {
    String value();
    Origin origin() default Origin.ANY;
}
