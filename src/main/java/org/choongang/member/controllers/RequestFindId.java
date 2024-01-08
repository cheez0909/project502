package org.choongang.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestFindId(
        @NotBlank @Email
        String email,

        @NotBlank
        String name
) {
}
