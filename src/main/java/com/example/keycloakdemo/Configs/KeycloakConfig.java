package com.example.keycloakdemo.Configs;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class KeycloakConfig {

    static Keycloak keycloak = null;
    final static String serverUrl = "https://localhost:8443";
    final static String realm = "master";
    final static String clientId = "admin-cli";
    final static String userName = "admin";
    final static String password = "admin";

    public KeycloakConfig() {
    }

    public static Keycloak getInstance(){
        if(keycloak == null){

            Client client = ClientBuilder.newClient();

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .resteasyClient(client)
                    .build();
        }
        return keycloak;
    }
}
