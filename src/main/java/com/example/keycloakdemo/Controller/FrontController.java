package com.example.keycloakdemo.Controller;

import com.example.keycloakdemo.DTO.UserDTO;
import com.example.keycloakdemo.Service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Scanner;

import static com.example.keycloakdemo.Util.Credentials.setUserInformation;

@Controller
public class FrontController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView homePage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"home");
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }

    @GetMapping("/user")
    public ModelAndView userPage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"user");
    }

    @GetMapping("/profile")
    public ModelAndView profilePage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"profile");
    }

    @GetMapping("/roles")
    public ModelAndView rolesPage(KeycloakAuthenticationToken token){
        return setUserInformation(token,"roles");
    }

    @GetMapping("/public")
    @ResponseBody
    public String publicPage(){
        return "Welcome public";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createUser(UserDTO userDTO) throws Exception {
        return userService.addUser(userDTO) ;
    }
}
