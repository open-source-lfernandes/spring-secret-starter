package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * AbstractProperties is an abstract class that serves as a base for properties
 * configuration in the secrets management system. It contains common fields
 * such as enabled status and order.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractProperties {
    Boolean enabled;
    Integer order;
}
