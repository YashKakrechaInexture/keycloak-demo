package com.example.keycloakdemo.Controller;

import com.example.keycloakdemo.DTO.UserDTO;
import com.example.keycloakdemo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Scanner;

@Controller
public class FrontController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(){
        return "home";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }

    @GetMapping("/user")
    public String userPage(){
        return "user";
    }

    @GetMapping("/public")
    @ResponseBody
    public String publicPage(){
        return "Welcome public";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createUser(UserDTO userDTO) throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("demo");
//        userDTO.setPassword("demo123");
//        userDTO.setEmail("demo@gmail.com");
//        userDTO.setFirstname("demo");
//        userDTO.setLastname("demo");
        return userService.addUser(userDTO) ;
    }
}
