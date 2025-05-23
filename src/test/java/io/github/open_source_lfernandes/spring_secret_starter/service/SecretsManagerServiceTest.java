package io.github.open_source_lfernandes.spring_secret_starter.service;

import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.exceptions.SecretNotFoundException;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProvider;
import io.github.open_source_lfernandes.spring_secret_starter.service.providers.SecretsProviderAws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SecretsManagerServiceTest {

    SecretsManagerService secretsManagerService;

    @Mock
    SecretsProviderAws secretsProviderAws;

    @BeforeEach
    void setUpProviders() {
        final Set<SecretsProvider> providers = Set.of(secretsProviderAws);
        secretsManagerService = new SecretsManagerService(providers);
    }

    @Test
    void shouldReturnSecretFromList() {
        final var key = "key";
        final var secretDTOExpected = SecretDTO.builder()
                .key(key)
                .value("secret")
                .build();
        when(secretsProviderAws.get(key)).thenReturn(secretDTOExpected);

        Set<SecretDTO> setKeysResponse = secretsManagerService.get(key);

        assertThat(setKeysResponse)
                .isNotEmpty()
                .contains(secretDTOExpected);
    }

    @Test
    void shouldNotReturnSecretFromList() {
        when(secretsProviderAws.get(anyString())).thenReturn(null);

        Set<SecretDTO> setKeysResponse = secretsManagerService.get("key");

        assertThat(setKeysResponse).isEmpty();
    }

    @Test
    void shouldReturnSecretFromProviderAws() {
        final var key = "key";
        final var secretDTOExpected = SecretDTO.builder()
                .key(key)
                .value("secret")
                .build();
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(key)).thenReturn(secretDTOExpected);

        Optional<SecretDTO> optionalSecretDTO = secretsManagerService.get(Origin.AWS, key);

        assertFalse(optionalSecretDTO.isEmpty());
        assertEquals(secretDTOExpected, optionalSecretDTO.get());
    }

    @Test
    void shouldReturnOptionalSecretEmptyFromProviderAws() {
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(anyString())).thenReturn(null);

        Optional<SecretDTO> optionalSecretDTO = secretsManagerService.get(Origin.AWS, "key");

        assertTrue(optionalSecretDTO.isEmpty());
    }

    @Test
    void shouldReturnSecretOrFailureSuccess() {
        final var key = "key";
        final var secretDTOExpected = SecretDTO.builder()
                .key(key)
                .value("secret")
                .build();
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(key)).thenReturn(secretDTOExpected);

        var secretDTO = secretsManagerService.getOrFailure(Origin.AWS, key);

        assertNotNull(secretDTO);
        assertEquals(secretDTOExpected, secretDTO);
    }

    @Test
    void shouldThrowSecretNotFoundException() {
        when(secretsProviderAws.getOrigin()).thenReturn(Origin.AWS);
        when(secretsProviderAws.get(anyString())).thenReturn(null);

        assertThrows(SecretNotFoundException.class, () -> secretsManagerService.getOrFailure(Origin.AWS, "key"));
    }
}
