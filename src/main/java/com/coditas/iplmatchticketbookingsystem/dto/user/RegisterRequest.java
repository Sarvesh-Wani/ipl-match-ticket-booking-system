package com.coditas.iplmatchticketbookingsystem.dto.user;


import com.coditas.iplmatchticketbookingsystem.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String name;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 4)
    private String password;


}