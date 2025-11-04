package com.example.app.DataTransferObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ProfileUpdate(

        @NotBlank String fullName,
        @NotBlank @Email String email

){}
