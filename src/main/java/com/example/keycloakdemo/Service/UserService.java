package com.example.keycloakdemo.Service;

import com.example.keycloakdemo.Configs.KeycloakConfig;
import com.example.keycloakdemo.DTO.UserDTO;
import com.example.keycloakdemo.Util.Credentials;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
public class UserService {
    public String addUser(UserDTO userDTO) throws Exception {
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = getInstance();
        Response response = instance.create(user);
        if(response.getStatus()!=HttpStatus.SC_CREATED){
            return "Exception Occured.";
        }
        return "User Created.";
    }
    public static UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm("demoRealm").users();
    }

}
