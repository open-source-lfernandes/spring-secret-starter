package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * CommonsSecretProperties is an abstract class that holds common properties for secret management.
 * <br>
 * It contains a list of keys and a flag to indicate if secrets should be enabled on startup.
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractSecretProperties {
    Boolean enabled;
    List<String> keys;
    Boolean cachingOnStartup;
}
