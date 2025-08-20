package com.example.study_log.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    private Long id;
    private String nickname;
    private String profile_image;
    private String connected_at;

    public KakaoInfoDto(Map<String, Object> attributes) {
        // 1. 최상위 레벨에서 'id'와 'connected_at' 추출
        this.id = Long.valueOf(attributes.get("id").toString());
        this.connected_at = (String) attributes.get("connected_at");

        // 2. 'kakao_account' Map 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        // 3. 'kakao_account' 내부의 'profile' Map 추출
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // 4. 'profile' Map에서 'nickname'과 'profile_image_url' 추출
        this.nickname = (String) profile.get("nickname");
        this.profile_image = (String) profile.get("profile_image_url");

        // 만약 'profile'이 null이거나 필요한 정보가 없다면, 'properties'에서 가져오는 폴백(fallback) 로직 추가
        if (this.nickname == null && attributes.get("properties") != null) {
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            this.nickname = (String) properties.get("nickname");
            this.profile_image = (String) properties.get("profile_image");
        }
    }
}