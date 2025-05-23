package io.github.open_source_lfernandes.spring_secret_starter.messages;

import lombok.Getter;

/**
 * Messages is an enum that contains error messages used in the application.
 * Each enum constant represents a specific error message.
 */
@Getter
public enum Messages {

    KEY_CANNOT_BE_NULL("Key cannot be null"),
    ORIGIN_CANNOT_BE_BULL("Origin cannot be null");

    private final String description;

    Messages(String description) {
        this.description = description;
    }

}
