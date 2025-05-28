package io.github.open_source_lfernandes.spring_secret_starter.dto;

import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import lombok.Builder;

/**
 * SecretDTO is a data transfer object that represents a secret.
 * It contains the origin, key, and value of the secret.
 *
 * <p>The {@code value} field represents the secret's value in JSON format.
 * This ensures that complex objects can be serialized and stored as a string.
 */
@Builder
public record SecretDTO(Origin origin, String key, String value) {
}
