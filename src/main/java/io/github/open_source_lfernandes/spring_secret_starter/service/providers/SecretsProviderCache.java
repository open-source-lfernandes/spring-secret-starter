package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SecretsProviderCache implements SecretsProvider {

    private final ConcurrentHashMap<String, SecretDTO> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Origin getOrigin() {
        return Origin.CACHE;
    }

    @Override
    public Optional<SecretDTO> get(String key) {
        return Optional.of(cacheMap.get(key));
    }

    @Override
    public SecretDTO put(SecretDTO secretDTO) {
        cacheMap.put(createKey(secretDTO.key()), secretDTO);
        return secretDTO;
    }

    @Override
    public void delete(String key) {
        cacheMap.remove(key);
    }

    public void clearCache() {
        cacheMap.clear();
    }

}
