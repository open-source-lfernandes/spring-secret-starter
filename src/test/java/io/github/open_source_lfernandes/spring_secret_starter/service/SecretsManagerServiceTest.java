package io.github.open_source_lfernandes.spring_secret_starter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.utils.faker.Credential;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.AbstractSecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderAws;
import io.github.open_source_lfernandes.spring_secret_starter.utils.JsonUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SecretsManagerServiceTest {

    SecretsManagerService secretsManagerService;

    @Mock
    SecretsProviderAws secretsProviderAws;

    CustomSecretsProvider customSecretsProvider = new CustomSecretsProvider(1);

    @BeforeEach
    void setUpProviders() {
        final List<AbstractSecretsProvider> providers = List.of(secretsProviderAws, customSecretsProvider);
        secretsManagerService = new SecretsManagerService(providers, new ObjectMapper());
    }

    @Test
    void shouldReturnSecretFromList() {
        final var key = "key";
        final var secretDTOAwsExpected = SecretDTO.builder()
                .origin(Origin.AWS)
                .key(key)
                .value("secret")
                .build();

        final List<SecretDTO> listExpected = List.of(
                secretDTOAwsExpected,
                SecretDTO.builder()
                        .origin(Origin.CUSTOM)
                        .key(key)
                        .value("custom-value")
                        .build()
        );
        when(secretsProviderAws.get(key)).thenReturn(Optional.of(secretDTOAwsExpected));

        List<SecretDTO> setKeysResponse = secretsManagerService.get(key);

        assertThat(setKeysResponse)
                .isNotEmpty()
                .isEqualTo(listExpected);
    }

    @Test
    void shouldReturnSecretOnlyCustomProviderFromList() {
        final List<SecretDTO> listExpected = List.of(
                SecretDTO.builder()
                        .origin(Origin.CUSTOM)
                        .key("key")
                        .value("custom-value")
                        .build()
        );

        when(secretsProviderAws.get(anyString())).thenReturn(Optional.empty());

        List<SecretDTO> setKeysResponse = secretsManagerService.get("key");

        assertThat(setKeysResponse)
                .isNotEmpty()
                .isEqualTo(listExpected);
    }

    @Test
    void shouldReturnSecretFromProviderAws() {
        final var key = "key";
        final var secretDTOExpected = Optional.of(
                SecretDTO.builder()
                        .key(key)
                        .value("secret")
                        .build()
        );
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(key)).thenReturn(secretDTOExpected);

        Optional<SecretDTO> optionalSecretDTO = secretsManagerService.get(Origin.AWS, key);

        assertFalse(optionalSecretDTO.isEmpty());
        assertEquals(secretDTOExpected, optionalSecretDTO);
    }

    @Test
    void shouldReturnOptionalSecretEmptyFromProviderAws() {
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(anyString())).thenReturn(Optional.empty());

        Optional<SecretDTO> optionalSecretDTO = secretsManagerService.get(Origin.AWS, "key");

        assertTrue(optionalSecretDTO.isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldReturnSecretOrFailureSuccess() {
        final var key = "key";
        final var secretDTOExpected = Optional.of(
                SecretDTO.builder()
                        .key(key)
                        .value("secret")
                        .build()
        );
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(key)).thenReturn(secretDTOExpected);

        var secretDTO = secretsManagerService.getOrFailure(Origin.AWS, key);

        assertNotNull(secretDTO);
        assertEquals(secretDTOExpected.get(), secretDTO);
    }

    @Test
    @SneakyThrows
    void shouldReturnSecretAsObjectSuccess() {
        final var key = "key";
        final var credentialExpected = new Credential("lucas", "123456");
        final var optionalSecretDTO = Optional.of(
                SecretDTO.builder()
                        .key(key)
                        .value(JsonUtils.convertSecretValueToJson(credentialExpected))
                        .build()
        );
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(key)).thenReturn(optionalSecretDTO);

        Credential credentialResponseSecret = secretsManagerService.get(Origin.AWS, key, Credential.class);

        assertNotNull(credentialResponseSecret);
        assertEquals(credentialExpected, credentialResponseSecret);
    }

    @Test
    void shouldThrowSecretNotFoundException() {
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(anyString())).thenReturn(Optional.empty());

        assertThrows(SecretNotFoundException.class, () -> secretsManagerService.getOrFailure(Origin.AWS, "key"));
    }

    static class CustomSecretsProvider extends AbstractSecretsProvider {

        public CustomSecretsProvider(Integer order) {
            super(new ObjectMapper(), order);
        }

        @Override
        public Origin getOrigin() {
            return Origin.CUSTOM;
        }

        @Override
        public Optional<SecretDTO> get(String key) {
            return Optional.of(SecretDTO.builder()
                    .origin(Origin.CUSTOM)
                    .key(key)
                    .value("custom-value")
                    .build()
            );
        }
    }
}
