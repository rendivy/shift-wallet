package com.example.shiftwallet.dao.model.account;

import lombok.Builder;

import java.util.Date;

@Builder
public record RegistrationRequest(
        Gender gender,
        Integer age,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
