package com.example.shiftwallet.dao.repository.mappers;

import com.example.shiftwallet.dao.model.account.ProfileResponse;
import com.example.shiftwallet.dao.model.account.RegistrationRequest;
import com.example.shiftwallet.entity.User;
import com.example.shiftwallet.util.HashPassword;



public class UserMapper {

    public static User mapToUser(RegistrationRequest registerRequest, HashPassword hashPassword) {
        User user = new User();
        user.setAge(registerRequest.age());
        user.setPassword(registerRequest.password());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(registerRequest.email());
        user.setPhoneNumber(registerRequest.phoneNumber());
        user.setGender(registerRequest.gender());
        return user;
    }


    public static ProfileResponse mapToProfileDTO(User user) {
        return ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .walletId(user.getWalletId())
                .registrationDate(user.getRegistrationDate())
                .lastUpdateDate(user.getLastUpdateDate())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .age(user.getAge())
                .build();
    }

}