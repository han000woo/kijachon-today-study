package com.example.study_log.dto;

import com.example.study_log.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String platform;
    private String nickname;
    private String profileImage;
    private String refreshToken;

    public User convertUser(){
        return User.builder()
                .id(this.id)
                .platform(this.platform)
                .nickname(this.nickname)
                .profileImage(this.profileImage)
                .refreshToken(this.refreshToken)
                .build();
    }
}