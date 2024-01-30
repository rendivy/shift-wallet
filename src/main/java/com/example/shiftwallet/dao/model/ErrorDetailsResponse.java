package com.example.shiftwallet.dao.model;

import lombok.Builder;

@Builder
public record ErrorDetailsResponse(
        String message,
        Integer status
) {}

