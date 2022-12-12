package com.example.keycloakdemo.Service;

import com.example.keycloakdemo.Configs.KeycloakConfig;
import com.example.keycloakdemo.DTO.CredentialsDTO;
import com.example.keycloakdemo.DTO.RealmDTO;
import com.example.keycloakdemo.DTO.RoleDTO;
import com.example.keycloakdemo.DTO.UserDTO;
import com.example.keycloakdemo.Util.Credentials;
import org.apache.http.HttpStatus;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHContentBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {

//    @Value("${keycloak.realm}")
//    private String realm;
//    = "demoRealm"

//    @Value("${keycloak.resource}")
//    private String clientId;
//    = "demoClient"

    @Value("${home.url}")
    private String homeURL;

    public String addUser(UserDTO userDTO) throws Exception {
        if(userDTO.getRealm()=="master"){
            return "Exception Occured.";
        }
        CredentialRepresentation credential = Credentials
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
//        List<String> list = new ArrayList<>();
//        list.add("java");
//        user.setRealmRoles(list);

        UsersResource instance = getInstance(userDTO.getRealm());
        Response response = instance.create(user);
//        this.addRealmRoleToUser(user.getUsername(),"java");
        if(response.getStatus()!=HttpStatus.SC_CREATED){
            return "Exception Occured.";
        }
        return "User Created.";
    }
    public UsersResource getInstance(String realmName){
        return KeycloakConfig.getInstance().realm(realmName).users();
    }

        public void addRealmRoleToUser(String userName, String role_name, String realmName){
        Keycloak keycloak = KeycloakConfig.getInstance();
//        String client_id = keycloak
//                .realm(realmName)
//                .clients()
//                .findByClientId(clientId)
//                .get(0)
//                .getId();
        String userId = keycloak
                .realm(realmName)
                .users()
                .search(userName)
                .get(0)
                .getId();
        UserResource user = keycloak
                .realm(realmName)
                .users()
                .get(userId);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
//
//        Client level role :
//
//        RolesResource rolesResource = keycloak
//                .realm(realmName)
//                .clients()
//                .get(client_id)
//                .roles();
//        RoleResource roleResource = rolesResource.get(role_name);
//        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
//        roleToAdd.add(roleRepresentation);
//        user.roles().clientLevel(client_id).add(roleToAdd);
//
//        Realm level role :
//
        RolesResource rolesResource = keycloak
                .realm(realmName)
                .roles();
        RoleResource roleResource = rolesResource.get(role_name);
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        roleToAdd.add(roleRepresentation);
        user.roles().realmLevel().add(roleToAdd);
    }
    public List<RoleDTO> getAllRealmRoles(String userName,String realmName){
        Keycloak keycloak = KeycloakConfig.getInstance();
        RolesResource rolesResource = keycloak
                .realm(realmName)
                .roles();
        List<RoleRepresentation> roles = rolesResource.list();
        List<RoleRepresentation> userRoles = getUserRealmRoles(userName,realmName);
        Set<String> set = new HashSet<>();

        for(RoleRepresentation roleRepresentation : userRoles){
            set.add(roleRepresentation.getName());
        }

        List<RoleDTO> roleDTOList = new ArrayList<>();
        for(RoleRepresentation roleRepresentation : roles){
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(roleRepresentation.getName());
            roleDTO.setAssign(set.contains(roleRepresentation.getName()));
            roleDTOList.add(roleDTO);
        }
        return roleDTOList;
    }
    public List<RoleRepresentation> getUserRealmRoles(String userName, String realmName){
        Keycloak keycloak = KeycloakConfig.getInstance();
        String userId = keycloak
                .realm(realmName)
                .users()
                .search(userName)
                .get(0)
                .getId();
        UserResource user = keycloak
                .realm(realmName)
                .users()
                .get(userId);
        return user.roles().realmLevel().listAll();
    }

    public List<RealmRepresentation> getAllRealmNames(){
        Keycloak keycloak = KeycloakConfig.getInstance();
        List<RealmRepresentation> realmRepresentationsList = keycloak.realms().findAll();
        realmRepresentationsList.removeIf(realmRepresentation -> realmRepresentation.getRealm().equals("master"));
        return realmRepresentationsList;
    }
    public String createRealm(String realmName) throws Exception {
        Keycloak keycloak = KeycloakConfig.getInstance();

        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realmName+"-realm");
        realmRepresentation.setEnabled(true);

        List<String> redirectURLs = new ArrayList<>();
        redirectURLs.add(homeURL+"/*");

        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setName(realmName+"Client");
        clientRepresentation.setId(realmName+"Client");
        clientRepresentation.setRootUrl(homeURL);
        clientRepresentation.setRedirectUris(redirectURLs);

        try {
            keycloak.realms().create(realmRepresentation);
            keycloak.realms().realm(realmRepresentation.getRealm()).clients().create(clientRepresentation);
            ClientResource clientResource = keycloak.realms().realm(realmName + "-realm").clients().get(realmName + "Client");
            ClientRepresentation clientRepresentation1 = clientResource.toRepresentation();
            System.out.println(clientRepresentation1.getSecret());
            RealmDTO realmDTO = new RealmDTO();
            realmDTO.setRealm(realmName + "-realm");
            realmDTO.setResource(realmName + "Client");
            CredentialsDTO credentialsDTO = new CredentialsDTO();
            credentialsDTO.setSecret(clientRepresentation1.getSecret());
            realmDTO.setCredentialsDTO(credentialsDTO);
            realmDTO.setAuthServerUrl("https://localhost:8443");
            realmDTO.setPublicClient(false);
            if(!uploadInGithub(realmDTO.toString(),realmName)){
                keycloak.realms().realm(realmRepresentation.getRealm()).remove();
                return "Exception : Realm already exist or github credentials are wrong.";
            }
        }catch (Exception e){
            System.out.println(e);
            return e.getMessage();
        }
        return "Created";
    }
    private boolean uploadInGithub(String fileContent, String fileName) throws IOException {
        GitHub gitHub = GitHub.connect("YashKakrechaInexture","{your_token}");
        if(gitHub.isCredentialValid()){
            GHRepository repository = gitHub.getRepository("YashKakrechaInexture/realms-repo");
            try {
                GHContent content = repository.getFileContent(fileName+"-realm.json");
                System.out.println("File is already present in github.");
                return false;
            }catch(Exception e){
                System.out.println("File not found in github.");
            }
            GHContentBuilder contentBuilder = repository.createContent();
            contentBuilder.path(fileName+"-realm.json").content(fileContent).message(fileName+" realm file commit").commit();
            return true;
        }
        return false;
    }
}
