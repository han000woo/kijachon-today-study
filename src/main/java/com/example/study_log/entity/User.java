package com.example.study_log.entity;

import com.example.study_log.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="study_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "platform")
    private String platform;

    @Column(name = "refresh_token")
    private String refreshToken;

    public UserDto convertUserDto(){
        return UserDto.builder()
                .id(this.id)
                .nickname(this.nickname)
                .profileImage(this.profileImage)
                .platform(this.platform)
                .refreshToken(this.refreshToken)
                .build();
    }

    public void update(UserDto userDto) {
        if(userDto.getNickname() != null) this.nickname = userDto.getNickname();
        if(userDto.getProfileImage() != null) this.profileImage = userDto.getProfileImage();
        if(userDto.getPlatform() != null) this.platform = userDto.getPlatform();
        if(userDto.getRefreshToken() != null) this.refreshToken = userDto.getRefreshToken();
    }

    public void updateRefreshToken(String refreshToken){
        if(refreshToken != null) this.refreshToken = refreshToken;
    }
}
