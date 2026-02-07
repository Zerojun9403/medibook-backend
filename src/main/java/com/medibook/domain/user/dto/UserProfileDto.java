package com.medibook.domain.user.dto;

import com.medibook.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String birthdate;
    private String role;

    public static UserProfileDto from(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthdate(user.getBirthdate())
                .role(user.getRole().name())
                .build();
    }
}