package io.github.open_source_lfernandes.spring_secret_starter.messages;

import lombok.Getter;

/**
 * Messages is an enum that contains error messages used in the application.
 * Each enum constant represents a specific error message.
 */
@Getter
public enum Messages {

    /**
     * Error message for when the type is null.
     */
    TYPE_CANNOT_BE_NULL("Type cannot be null"),
    /**
     * Error message for when the secret value is not found.
     */
    JSON_PARSE_SECRET_VALUE_ERROR("Error parsing secret value from JSON"),
    /**
     * Error messages used in the application.
     */
    KEY_CANNOT_BE_NULL("Key cannot be null"),
    /**
     * Error message for when the secret value is null.
     */
    ORIGIN_CANNOT_BE_BULL("Origin cannot be null");

    private final String description;

    Messages(String description) {
        this.description = description;
    }

}
