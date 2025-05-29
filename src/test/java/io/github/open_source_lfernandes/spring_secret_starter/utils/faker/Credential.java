package io.github.open_source_lfernandes.spring_secret_starter.utils.faker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credential implements Serializable {
    String username;
    String password;
}