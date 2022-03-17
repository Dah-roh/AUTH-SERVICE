package com.reloadly_task.authservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
}
