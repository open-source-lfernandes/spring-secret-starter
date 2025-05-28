package io.github.open_source_lfernandes.spring_secret_starter.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@UtilityClass
public class JsonUtils {

    @SneakyThrows
    public String convertSecretValueToJson(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }
}
