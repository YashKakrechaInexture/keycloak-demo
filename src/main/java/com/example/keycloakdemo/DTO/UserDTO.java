package com.example.keycloakdemo.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}