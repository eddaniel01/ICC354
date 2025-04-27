package com.ejemplo.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthenticationResponse {
    private Long id;
    private String token;
    private String username;
    private String role;
}
