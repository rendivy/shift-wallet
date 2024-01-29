package com.example.shiftwallet.dao.model.account;

import lombok.Builder;

import java.util.Date;
import java.util.UUID;

@Builder
public record ProfileResponse(
        UUID id,
        String email,
        UUID walletId,
        String firstName,
        String lastName,
        Date lastUpdateDate,
        Date registrationDate,
        String phoneNumber,
        Gender gender,
        Integer age
){}
