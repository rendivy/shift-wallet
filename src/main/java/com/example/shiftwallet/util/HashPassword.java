package com.example.shiftwallet.util;

@FunctionalInterface
public interface HashPassword {
    String hash(String password);
}
