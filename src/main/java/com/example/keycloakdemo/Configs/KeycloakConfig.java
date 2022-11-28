package com.example.keycloakdemo.Configs;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.ws.rs.client.ClientBuilder;

public class KeycloakConfig {

    static Keycloak keycloak = null;
    final static String serverUrl = "https://localhost:8443";
    final static String realm = "master";
    final static String clientId = "admin-cli";
    final static String clientSecret = "";
    final static String userName = "admin";

    final static String password = "admin";

    public KeycloakConfig() {
    }

    public static Keycloak getInstance(){
        if(keycloak == null){

            ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(client)
                    .build();
        }
        return keycloak;
    }
}
