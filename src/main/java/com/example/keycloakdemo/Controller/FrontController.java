package com.example.keycloakdemo.Controller;

import com.example.keycloakdemo.DTO.UserDTO;
import com.example.keycloakdemo.Service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.example.keycloakdemo.Util.Credentials.setUserInformation;

@Controller
public class FrontController {

    @Autowired
    private UserService userService;

    @GetMapping("/tenant/{realm}")
    public ModelAndView loginPage(@PathVariable String realm, KeycloakAuthenticationToken token){
        return setUserInformation(token,"user");
    }

    @GetMapping("/tenant/{realm}/index")
    public ModelAndView homePage(KeycloakAuthenticationToken token){
        ModelAndView modelAndView = setUserInformation(token,"home");
        modelAndView.addObject("realmList",userService.getAllRealmNames());
        return modelAndView;
    }

    @GetMapping("/tenant/{realm}/admin")
    public ModelAndView  adminPage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"home");
    }

    @GetMapping("/tenant/{realm}/user")
    public ModelAndView userPage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"user");
    }

    @GetMapping("/tenant/{realm}/profile")
    public ModelAndView profilePage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"profile");
    }

    @GetMapping("/tenant/{realm}/roles")
    public ModelAndView rolesPage(KeycloakAuthenticationToken token){
        ModelAndView modelAndView = setUserInformation(token,"roles");
        modelAndView.addObject("realmRoles",userService.getAllRealmRoles(((UserDTO)modelAndView.getModel().get("user")).getUsername(),(String) modelAndView.getModel().get("realm")+"-realm"));
        return modelAndView;
    }

    @GetMapping("/tenant/{realm}/public")
    @ResponseBody
    public String publicPage(){
        return "Welcome public";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createUser(UserDTO userDTO) throws Exception {
        return userService.addUser(userDTO) ;
    }

    @GetMapping("/tenant/{realm}/addRealmRole/{name}")
    @ResponseBody
    public String addRealmRole(@PathVariable String name,
                               KeycloakAuthenticationToken token){
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String issuer = accessToken.getIssuer();
        String realm = issuer.substring(issuer.length()-8);
        userService.addRealmRoleToUser(accessToken.getPreferredUsername(),name,realm);
        return name + " role Added.";
    }
}
