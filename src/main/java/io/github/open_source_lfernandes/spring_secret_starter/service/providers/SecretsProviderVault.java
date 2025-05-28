package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
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
        Map<String, Object> mapDataKeySecret = (Map<String, Object>) response.getData().get(DEFAULT_KEY_RESPONSE);

        if (mapDataKeySecret.containsKey(key)) {
            return Optional.of(
                    SecretDTO.builder()
                            .key(key)
                            .value(convertSecretValueToJson(mapDataKeySecret.get(key)))
                            .build()
            );
        }
        return Optional.empty();
    }

    /**
     * Updates the path in Vault where secrets are stored.
     *
     * @param path the new path in Vault
     */
    public void updatePath(String path) {
        this.path = path;
    }

}
