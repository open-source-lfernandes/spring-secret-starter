package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.CannotCastTypeException;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.messages.Messages;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.Optional;


/**
 * SecretsProviderVault is an implementation of the SecretsProvider interface
 * that retrieves secrets from HashCorp Vault.
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecretsProviderVault extends AbstractSecretsProvider {

    /**
     * Default path for storing secrets in Vault.
     * This is the standard path used by Vault's KV (Key-Value) secrets engine.
     */
    private static final String DEFAULT_PREFIX_PATH = "secret/data/";

    /**
     * Default key for the response data in Vault.
     */
    private static final String DEFAULT_KEY_RESPONSE = "data";

    /**
     * The VaultTemplate for interacting with Vault.
     */
    final VaultTemplate vaultTemplate;

    /**
     * The path in Vault where secrets are stored.
     */
    String path;

    /**
     * Constructs a SecretsProviderVault with the specified order, VaultTemplate, and path.
     *
     * @param objectMapper  the ObjectMapper for JSON serialization/deserialization
     * @param order         the order of the provider, used to determine the precedence of secret retrieval
     * @param vaultTemplate the VaultTemplate for interacting with Vault
     * @param path          the path in Vault where secrets are stored
     */
    public SecretsProviderVault(ObjectMapper objectMapper, Integer order, VaultTemplate vaultTemplate, String path) {
        super(objectMapper, order);
        this.vaultTemplate = vaultTemplate;
        this.path = path;
    }

    @Override
    public Origin getOrigin() {
        return Origin.VAULT;
    }

    @Override
    public Optional<SecretDTO> get(String key) {
        final VaultResponse response = vaultTemplate.read(path);
        Map<String, Object> mapDataKeySecret = readMapDataKeySecret(response);

        if (mapDataKeySecret.containsKey(key)) {
            return Optional.of(
                    SecretDTO.builder()
                            .key(key)
                            .value(mapDataKeySecret.get(key).toString())
                            .build()
            );
        }
        return Optional.empty();
    }

    @Override
    public <T> T get(String key, Class<T> type) throws SecretNotFoundException {
        final VaultResponse response = vaultTemplate.read(path);
        Map<String, Object> mapDataKeySecret = readMapDataKeySecret(response);
        if (mapDataKeySecret.containsKey(key)) {
            String value = mapDataKeySecret.get(key).toString();
            try {
                return convertJsonStringToTypeInstance(value, type);
            } catch (Exception e) {
                log.error("Error converting secret value to type {}: {}", type.getName(), e.getMessage(), e);
                throw new CannotCastTypeException(e);
            }
        }
        throw new SecretNotFoundException(Messages.SECRET_NOT_FOUND.getDescription());
    }

    /**
     * Reads the map data from the Vault response.
     *
     * @param response the Vault response containing the secret data
     * @return a map containing the secret data, or an empty map if the response is null or does not contain data
     */
    private Map<String, Object> readMapDataKeySecret(VaultResponse response) {
        if (response == null || response.getData() == null) {
            logError();
            return Map.of();
        }
        return (Map<String, Object>) response.getData().get(DEFAULT_KEY_RESPONSE);
    }

    /**
     * Logs an error message indicating that the secret was not found in Vault.
     */
    void logError() {
        log.error("stage=secret-not-found-in-vault, path={}", path);
    }

    /**
     * Updates the path in Vault where secrets are stored.
     * By default, the path is prefixed with "secret/data/".
     * @param path the new path to set
     */
    public void updatePath(String path) {
        this.path = DEFAULT_PREFIX_PATH.concat(path);
    }

}
