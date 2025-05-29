package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static java.util.Objects.nonNull;

/**
 * AbstractProperties is an abstract class that serves as a base for properties
 * configuration in the secrets management system. It contains common fields
 * such as enabled status and order.
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractProperties {
    /**
     * Indicates whether the property is enabled.
     */
    Boolean enabled;
    /**
     * The order of this property in relation to others.
     * Lower values indicate higher priority.
     */
    Integer order;

    /**
     * Constructor to initialize AbstractProperties with the specified parameters.
     *
     * @param enabled whether the property is enabled
     * @param order   the order of this property in relation to others
     */
    public AbstractProperties(Boolean enabled, Integer order) {
        this.enabled = Boolean.TRUE.equals(enabled) ? Boolean.TRUE : Boolean.FALSE;
        this.order = nonNull(order) ? order : Integer.MAX_VALUE;
    }
}