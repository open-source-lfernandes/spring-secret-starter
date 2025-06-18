# Spring Secret Starter

![Java](https://img.shields.io/badge/Java-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1+-brightgreen)

A Spring Boot starter for seamless integration with secret management services like AWS Secrets Manager, HashiCorp
Vault, and more.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Providers](#providers)
    - [AWS Secrets Manager](#aws-secrets-manager)
    - [HashiCorp Vault](#hashicorp-vault)
    - [Custom](#custom)
- [Usage](#usage)
    - [AWS Secrets Manager](#aws-secrets-manager)
    - [HashiCorp Vault](#hashicorp-vault)
    - [Custom](#custom)
- [Configuration Properties](#configuration-properties)
- [Specifying Provider Order](#specifying-provider-order)
- [Examples](#examples)
    - [Accessing Secrets in Code using SecretsManagerService](#accessing-secrets-in-code-using-secretsmanagerservice)
    - [Retrieving a Secret by Key and Origin with Type Conversion](#retrieving-a-secret-by-key-and-origin-with-type-conversion)
    - [SecretDTO Explanation](#secretdto-explanation)
- [Next Steps](#next-steps)
- [Contributing](#contributing)

## Features

- **Abstract Secret Retrieval Logic**: Decoupled core implementation with provider-specific abstraction layer for easy
  extensibility
- **Multi-provider Support**: Integrate with AWS Secrets Manager, HashiCorp Vault, and other secret management services
- **Custom Providers**: Easily extend with your own secret provider implementations
- **Provider Order Configuration**: Specify the order in which providers are executed for secret retrieval using
  configuration properties
- **Type Conversion for Secrets**: Retrieve secrets by key and origin, converting the value to the specified type for seamless integration with your application.
- **@SecretValue Annotation**: Annotate fields to automatically inject secrets from the configured providers

## Installation

Add the dependency to your `pom.xml`:

```xml

<dependency>
    <groupId>com.github.open-source-lfernandes</groupId>
    <artifactId>spring-secret-starter</artifactId>
    <version>1.2.0</version>
</dependency>
```

Or for Gradle:

```groovy
implementation 'com.github.open-source-lfernandes:spring-secret-starter:1.2.0'
```

## Providers

### AWS Secrets Manager

AWS Secrets Manager is a service that helps you protect access to your applications, services, and IT resources without
the upfront investment and on-going maintenance costs of operating your own infrastructure.

For more information, visit the [AWS Secrets Manager Documentation](https://docs.aws.amazon.com/secretsmanager).

### HashiCorp Vault

HashiCorp Vault is a tool for securely accessing secrets. It provides a unified interface to any secret, while maintaining tight
access control and recording a detailed audit log.

For more information, visit the [HashiCorp Vault Documentation](https://www.vaultproject.io/docs).

### Custom

You can create your own custom secret provider by implementing the `SecretProvider` interface. This allows you to
integrate with any secret management service of your choice.

## Usage

### AWS-Secrets-Manager

Add the following to your `application.yml`:

```yaml
spring:
  secrets:
    aws:
      secrets-manager:
        enabled: true
        region: us-east-1
````

### HashiCorp Vault
Add the following to your `application.yml`:

```yaml
spring:
  secrets:
    vault:
      enabled: true
      uri: http://localhost:8200
      token: your-vault-token
```

### Custom

To use a custom secret provider, extends the `AbstractSecretsProvider` class and register it as a Spring bean. The
starter will automatically detect and use your custom provider.

```java 
import java.util.Optional;

@Component
public class SecretsProviderCustom extends AbstractSecretsProvider {

    private final YourCustomService yourCustomService;

    public SecretsProviderCustom(Integer order, YourCustomService yourCustomService) {
        super(order);
        this.yourCustomService = yourCustomService;
    }


    @Override
    public Origin getOrigin() {
        return Origin.CUSTOM;
    }

    @Override
    public Optional<SecretDTO> get(String key) {
        return Optional.of(
                SecretDTO.builder()
                        .origin(getOrigin())
                        .key(key)
                        .value(yourCustomService.getSecretValue(key))
                        .build()
        );
    }
}

```

### Using `@SecretValue` Annotation

The `@SecretValue` annotation allows you to inject secret values directly into your Spring beans. It simplifies the process of retrieving secrets by automatically resolving and injecting them from the configured secret providers.

#### Example 1: Injecting a String Secret
```java
import io.github.open_source_lfernandes.spring_secret_starter.annotations.SecretValue;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {

  @SecretValue("${example.secret-key}")
  private String secretValue;

  public void printSecret() {
    System.out.println("Secret Value: " + secretValue);
  }
}
```

#### Example 1: Injecting a Custom Object Secret
```java
import io.github.open_source_lfernandes.spring_secret_starter.annotations.SecretValue;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {

  @SecretValue(value = "${example.credential}", type = Credential.class)
  private Credential credential;

  public void printCredential() {
    System.out.println("Username: " + credential.getUsername());
    System.out.println("Password: " + credential.getPassword());
  }
}
```

#### How It Works
- The @SecretValue annotation retrieves the secret value from the configured providers based on the key specified in the value attribute.
- The type attribute allows you to specify the class type for type conversion (default is String).
- The secret is automatically injected into the annotated field during the Spring context initialization.

## Configuration Properties

The following table explains the configuration properties available for the Spring Secret Starter:

| Property                                      | Type      | Description                            | Default Value     |
|-----------------------------------------------|-----------|----------------------------------------|-------------------|
| `spring.secrets.aws.secrets-manager.enabled`  | `Boolean` | Enable AWS Secrets Manager.            | false             |
| `spring.secrets.aws.secrets-manager.region`   | `String`  | AWS Region.                            | us-east-1         |
| `spring.secrets.aws.secrets-manager.endpoint` | `String`  | AWS Endpoint.                          |                   |
| `spring.secrets.aws.secrets-manager.order`    | `Integer` | Providers Order that will be executed. | Integer.MAX_VALUE |
| `spring.secrets.vault.enabled`                | `Boolean` | Enable Vault.                          | false             |
| `spring.secrets.vault.token`                  | `String`  | Vault Token.                           |                   |
| `spring.secrets.vault.uri`                    | `String`  | Vault Uri.                             |                   |
| `spring.secrets.vault.order`                  | `Integer` | Providers Order that will be executed. | Integer.MAX_VALUE |

## Specifying Provider Order

To specify the order in which providers are executed, set the *order* property in your *application.yml* or
*application.properties* file. Providers with lower order values are executed first. Example:

Example:

```yaml
spring:
  secrets:
    aws:
      secrets-manager:
        order: 1
        enabled: true
        region: us-east-1
    vault:
      order: 2
      enabled: true
      uri: http://localhost:8200
      token: your-vault-token
```        

## Examples

### Accessing Secrets in Code using SecretsManagerService

```java
public class MyService {
    private final SecretsManagerService secretsManagerService;

    public MyService(SecretsManagerService secretsManagerService) {
        this.secretsManagerService = secretsManagerService;
    }

    public void doSomething() {
        // Getting secret value, not matter the provider
        Set<SecretDTO> secrets = secretsManagerService.get("my-secret-key");

        // Getting optional secret value, only for AWS Secrets Manager
        Optional<SecretDTO> optionalSecretDTO = secretsManagerService.get(Origin.AWS, "my-secret-key");

        // Getting secret value or failure, only for AWS Secrets Manager
        SecretDTO secretDTO = secretsManagerService.getOrFailure(Origin.AWS, "my-secret-key");
    }
}
```

### Retrieving a Secret by Key and Origin with Type Conversion

You can retrieve a secret by its key and origin and convert its value to a specific type using the `SecretsManagerService`. Here's an example:

```java
import io.github.open_source_lfernandes.spring_secret_starter.dto.SecretDTO;
import io.github.open_source_lfernandes.spring_secret_starter.enums.Origin;
import io.github.open_source_lfernandes.spring_secret_starter.service.SecretsManagerService;

public class MyService {

  private final SecretsManagerService secretsManagerService;

  public MyService(SecretsManagerService secretsManagerService) {
    this.secretsManagerService = secretsManagerService;
  }

  public MyCustomType getCustomSecret() {
    String key = "my-secret-key";
    Origin origin = Origin.AWS_SECRETS_MANAGER;

    return secretsManagerService.get(origin, key, MyCustomType.class);
  }
}

class MyCustomType {
  private String field1;
  private int field2;

  // Getters and setters
}
```

### SecretDTO Explanation

The SecretDTO class is a data transfer object (DTO) that represents a secret. It is implemented as a Java record, which is a compact and immutable data structure. Here's what the class represents:  

*Fields:*
- origin: Represents the source or origin of the secret (e.g., AWS Secrets Manager, HashiCorp Vault, etc.). It is of type Origin, which is likely an enum.
- key: The unique identifier or key for the secret.
- value: The actual value of the secret, stored in JSON format. This allows complex objects to be serialized and stored as a string.

*Purpose*:  
The class is designed to encapsulate the details of a secret in a structured way, making it easier to transfer and manipulate secret data within the application.

```java
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

```

## Next Steps

Next Providers to be implemented:

- Azure Key Vault
- Google Cloud Secret Manager
- Secret Cache Provider
    - Enabling caching of secrets to reduce the number of calls to the secret management service, improving performance
      and reducing costs.
    - Enabling caching on startup to preload secrets into the cache, ensuring they are available immediately when the
      application starts.

## Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (git checkout -b feature/your-feature)
3. Commit your changes (git commit -am 'Add some feature')
4. Push to the branch (git push origin feature/your-feature)
5. Open a Pull Request
