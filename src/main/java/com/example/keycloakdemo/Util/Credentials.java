package com.example.keycloakdemo.Util;

import com.example.keycloakdemo.DTO.UserDTO;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.web.servlet.ModelAndView;

public class Credentials {

    public static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public static ModelAndView setUserInformation(KeycloakAuthenticationToken token, String viewName) {
        UserDTO userDTO = null;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        if(token!=null) {
            KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
            KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
            AccessToken accessToken = session.getToken();

            userDTO = new UserDTO();
            userDTO.setFirstname(accessToken.getGivenName());
            userDTO.setLastname(accessToken.getFamilyName());
            userDTO.setEmail(accessToken.getEmail());
            userDTO.setUsername(accessToken.getPreferredUsername());
        }
        modelAndView.addObject("user",userDTO);
        return modelAndView;
    }
}
