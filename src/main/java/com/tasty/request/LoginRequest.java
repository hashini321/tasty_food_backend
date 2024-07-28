package com.tasty.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String password;
}
