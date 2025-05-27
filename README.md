# Spring Secret Starter

![Java](https://img.shields.io/badge/Java-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1+-brightgreen)

A Spring Boot starter for seamless integration with secret management services like AWS Secrets Manager, HashiCorp Vault, and more.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Providers](#providers)
  - [AWS Secrets Manager](#aws-secrets-manager)
  - [Custom](#custom)
- [Usage](#usage)
    - [AWS Secrets Manager](#aws-secrets-manager)
    - [Custom](#custom)
- [Configuration Properties](#configuration-properties)
- [Specifying Provider Order](#specifying-provider-order)
- [Examples](#examples)
- [Next Steps](#next-steps)
- [Contributing](#contributing)

## Features

- **Abstract Secret Retrieval Logic**: Decoupled core implementation with provider-specific abstraction layer for easy extensibility
- **Multi-provider Support**: Integrate with AWS Secrets Manager, HashiCorp Vault, and other secret management services
- **Custom Providers**: Easily extend with your own secret provider implementations
- **Provider Order Configuration**: Specify the order in which providers are executed for secret retrieval using configuration properties

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.open-source-lfernandes</groupId>
    <artifactId>spring-secret-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```
Or for Gradle:

```groovy
implementation 'com.github.open-source-lfernandes:spring-secret-starter:1.0.0'
```

## Providers

### AWS Secrets Manager
AWS Secrets Manager is a service that helps you protect access to your applications, services, and IT resources without the upfront investment and on-going maintenance costs of operating your own infrastructure.

For more information, visit the [AWS Secrets Manager Documentation](https://docs.aws.amazon.com/secretsmanager).

### Custom
You can create your own custom secret provider by implementing the `SecretProvider` interface. This allows you to integrate with any secret management service of your choice.

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
### Custom
To use a custom secret provider, extends the `AbstractSecretsProvider` class and register it as a Spring bean. The starter will automatically detect and use your custom provider.

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

## Configuration Properties

The following table explains the configuration properties available for the Spring Secret Starter:

| Property                                      | Type      | Description                            | Default Value |
|-----------------------------------------------|-----------|----------------------------------------|---------------|
| `spring.secrets.aws.secrets-manager.enabled`  | `Boolean` | Enable AWS Secrets Manager.            | false         |
| `spring.secrets.aws.secrets-manager.region`   | `String`  | AWS Region.                            | us-east-1     |
| `spring.secrets.aws.secrets-manager.endpoint` | `String`  | AWS Endpoint.                          |               |
| `spring.secrets.aws.secrets-manager.order`    | `Integer` | Providers Order that will be executed. | 99999         |

## Specifying Provider Order
To specify the order in which providers are executed, set the *order* property in your *application.yml* or *application.properties* file. Providers with lower order values are executed first.  Example:

Example:
```yaml
spring:
  secrets:
    aws:
      secrets-manager:
        enabled: true
        region: us-east-1
        order: 1
```        

## Examples

Accessing Secrets in Code using SecretsManagerService
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
## Next Steps
Next Providers to be implemented:
- HashiCorp Vault
- Azure Key Vault
- Google Cloud Secret Manager
- Secret Cache Provider
  - Enabling caching of secrets to reduce the number of calls to the secret management service, improving performance and reducing costs.
  - Enabling caching on startup to preload secrets into the cache, ensuring they are available immediately when the application starts.

## Contributing
We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (git checkout -b feature/your-feature)
3. Commit your changes (git commit -am 'Add some feature')
4. Push to the branch (git push origin feature/your-feature)
5. Open a Pull Request