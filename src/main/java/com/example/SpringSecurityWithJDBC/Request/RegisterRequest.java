package com.example.SpringSecurityWithJDBC.Request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String firstName;
    private String lastName;

    private String email;

    private String password;
}
