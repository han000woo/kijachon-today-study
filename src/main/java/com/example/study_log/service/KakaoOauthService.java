package com.example.study_log.service;

import com.example.study_log.config.OauthConfig;
import com.example.study_log.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {
    private final UserService userService;
    private final OauthConfig oauthConfig;


    // 카카오 Api를 호출하여 인가코드로 AccessToken 가져오기
    // 인가 코드로 카카오 액세스 토큰 요청
    public Map<String, Object> getAccessToken(String code) {

        // HTTP 요청 본문(Body)에 포함될 폼 데이터 설정
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", oauthConfig.getClientId());
        formData.add("client_secret",oauthConfig.getClientSecret());
        formData.add("redirect_uri", oauthConfig.getRedirectUri());
        formData.add("code", code);

        // WebClient를 사용하여 POST 요청 수행
        return WebClient.create()
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .headers(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    // 카카오Api 호출해서 AccessToken으로 유저정보 가져오기
    public Map<String, Object> getUserAttributesByToken(String accessToken){
        return WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    // 카카오API에서 가져온 유저정보를 DB에 저장
    public UserDto getUserProfileByToken(String accessToken){
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);
        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);
        UserDto userDto = UserDto.builder()
                .id(kakaoInfoDto.getId())
                .nickname(kakaoInfoDto.getNickname())
                .profileImage(kakaoInfoDto.getProfile_image())
                .platform("kakao")
                .build();
        if(userService.findById(userDto.getId()) != null) {
            userService.update(userDto);
        } else {
            userService.save(userDto);
        }
        return userDto;
    }

}