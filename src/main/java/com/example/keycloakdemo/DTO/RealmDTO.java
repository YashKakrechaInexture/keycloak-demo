package com.example.keycloakdemo.DTO;

import lombok.Data;

@Data
public class RealmDTO {
    String realm;
    String resource;
    CredentialsDTO credentialsDTO;
    String authServerUrl;
    boolean publicClient;

    @Override
    public String toString() {
        return "{\n" +
                "\t" + "\"realm\" : \"" + realm + '\"' + "," + '\n' +
                "\t" + "\"resource\" : \"" + resource + '\"' + "," + '\n' +
                "\t" + "\"credentials\" : " + credentialsDTO.toString() + "," + '\n' +
                "\t" + "\"auth-server-url\" : \"" + authServerUrl + '\"' + "," + '\n' +
                "\t" + "\"public-client\" : " + publicClient + '\n' +
                '}';
    }
}

