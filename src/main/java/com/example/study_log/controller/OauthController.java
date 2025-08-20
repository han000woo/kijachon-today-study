package com.example.study_log.controller;

import com.example.study_log.dto.OauthRequestDto;
import com.example.study_log.dto.OauthResponseDto;
import com.example.study_log.dto.RefreshTokenResponseDto;
import com.example.study_log.exception.CustomException;
import com.example.study_log.exception.ErrorCode;
import com.example.study_log.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/login/oauth/{provider}")
    public OauthResponseDto login(@PathVariable String provider, @RequestParam String code,
                                  HttpServletResponse response) {
        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        switch (provider) {
            case "kakao":
                //인가 코드를 가지고 카카오 서버로부터 accessToken 발급
                String kakaoAccessToken = oauthService.getAccessTokenFromKakao(code);

                //1. 카카오 서버로부터 accessToken으로 유저 정보 가져오기
                //2. 유저가 없다면 새 계정 생성, 서버 자체 accessToken, refreshToken 발급
                String accessToken = oauthService.loginWithKakao(kakaoAccessToken, response);
                oauthResponseDto.setAccessToken(accessToken);
        }
        return oauthResponseDto;
    }

    // 리프레시 토큰으로 액세스토큰 재발급 받는 로직
    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto tokenRefresh(HttpServletRequest request) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        Cookie[] list = request.getCookies();
        if(list == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);

        if(refreshTokenCookie == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
        refreshTokenResponseDto.setAccessToken(accessToken);
        return refreshTokenResponseDto;
    }
}
