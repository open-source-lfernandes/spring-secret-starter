package io.github.open_source_lfernandes.spring_secret_starter.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheProperties extends AbstractSecretProperties {
    public CacheProperties(Boolean enabled, List<String> keys, Boolean cachingOnStartup) {
        super(enabled, keys, cachingOnStartup);
    }
}
