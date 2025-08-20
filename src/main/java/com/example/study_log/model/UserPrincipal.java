package com.example.study_log.model;

import com.example.study_log.dto.UserDto;
import com.example.study_log.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
// SecurityContext authentication에 저장될 유저정보
public class UserPrincipal implements UserDetails {

    private long id;
    private String nickName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;

    public static UserPrincipal create(UserDto user) {
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(UserRole.USER.getRole()));
        return new UserPrincipal(
                user.getId(),
                user.getNickname(),
                "",
                authorities,
                null
        );
    }

    @Override
    public String getUsername() {
        return nickName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    @Override
//    public String getNickname() {
//        return nickName;
//    }

}