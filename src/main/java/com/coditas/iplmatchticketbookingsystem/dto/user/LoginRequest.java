package com.coditas.iplmatchticketbookingsystem.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
public class LoginRequest {

    @NotNull
    private String username;

    @Size(min = 4)
    private String password;


}
