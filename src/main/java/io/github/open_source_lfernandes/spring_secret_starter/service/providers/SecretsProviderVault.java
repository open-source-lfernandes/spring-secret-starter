package io.github.open_source_lfernandes.spring_secret_starter.service.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * SecretsProviderVault is an implementation of the SecretsProvider interface
 * that retrieves secrets from HashCorp Vault.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecretsProviderVault extends AbstractSecretsProvider {

    /**
     * The VaultTemplate for interacting with Vault.
     */
    VaultTemplate vaultTemplate;

    String path;

    /**
     * Constructs a SecretsProviderVault with the specified order, VaultTemplate, and path.
     *
     * @param order        the order of the provider, used to determine the precedence of secret retrieval
     * @param vaultTemplate the VaultTemplate for interacting with Vault
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
        VaultResponse response = vaultTemplate.read(path);
        Objects.requireNonNull(response, "Vault response cannot be null");

        Map<String , Object> mapDataKeySecret = (Map<String, Object>) response.getData().get("data");

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
}
