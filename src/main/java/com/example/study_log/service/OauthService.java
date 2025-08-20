package com.example.study_log.service;

import com.example.study_log.dto.UserDto;
import com.example.study_log.exception.CustomException;
import com.example.study_log.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final KakaoOauthService kakaoOauthService;

    //카카오 로그인
    public String loginWithKakao(String accessToken, HttpServletResponse response) {
        UserDto userDto = kakaoOauthService.getUserProfileByToken(accessToken);
        System.out.println("done here");
        return getTokens(userDto.getId(), response);
    }
    //카카오로부터 AccessToken 가져오기
    public String getAccessTokenFromKakao(String code) {
        Map<String, Object> kakaoInfo = kakaoOauthService.getAccessToken(code);

        //todo : 가져온 kakao 인증 정보를 사용하면 좋을 듯

        return kakaoInfo.get("access_token").toString();
    }
    //액세스토큰, 리프레시토큰 생성
    public String getTokens(Long id, HttpServletResponse response) {
        final String accessToken = jwtTokenService.createAccessToken(id.toString());
        final String refreshToken = jwtTokenService.createRefreshToken();

        System.out.println("done here 2");
        UserDto userDto = userService.findById(id).convertUserDto();
        System.out.println("done here 3");
        userDto.setRefreshToken(refreshToken);
        userService.updateRefreshToken(userDto);

        jwtTokenService.addRefreshTokenToCookie(refreshToken, response);
        return accessToken;
    }

    // 리프레시 토큰으로 액세스토큰 새로 갱신
    public String refreshAccessToken(String refreshToken) {
        UserDto userDto = userService.findByRefreshToken(refreshToken);
        if(userDto == null) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if(!jwtTokenService.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtTokenService.createAccessToken(userDto.getId().toString());
    }


}