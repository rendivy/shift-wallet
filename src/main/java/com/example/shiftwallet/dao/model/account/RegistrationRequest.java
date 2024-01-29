package com.example.shiftwallet.dao.model.account;

import lombok.Builder;

import java.util.Date;

@Builder
public record RegistrationRequest(
        Gender gender,
        String email,
        String password,
        Date dateOfBirth,
        String fullName,
        String phoneNumber
) {
}
