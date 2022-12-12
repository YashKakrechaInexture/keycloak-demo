package com.example.keycloakdemo.DTO;

import lombok.Data;

@Data
public class CredentialsDTO {
    String secret;

    @Override
    public String toString() {
        return "{" + '\n' +
                "\t" + "\t" + "\"secret\" : \"" + secret + '\"' + '\n' +
                "\t" + '}';
    }
}
