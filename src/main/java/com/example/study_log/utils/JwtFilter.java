package com.example.study_log.utils;

import com.example.study_log.config.OauthConfig;
import com.example.study_log.dto.UserDto;
import com.example.study_log.exception.CustomException;
import com.example.study_log.exception.ErrorCode;
import com.example.study_log.model.UserPrincipal;
import com.example.study_log.service.JwtTokenService;
import com.example.study_log.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final OauthConfig oauthConfig;

    // 요 Filter 에서 액세스토큰이 유효한지 확인 후 SecurityContext에 계정정보 저장
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse; // HttpServletResponse 추가

        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());
        String jwt = resolveToken(httpServletRequest);

        if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
            Long userId = Long.valueOf(jwtTokenService.getPayload(jwt)); // 토큰 Payload에 있는 userId 가져오기
            UserDto user = userService.findById(userId).convertUserDto(); // userId로
            if(user == null) {
                throw new CustomException(ErrorCode.NOT_EXIST_USER);
            }
            UserDetails userDetails = UserPrincipal.create(user);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // 토큰이 유효하지 않거나 없는 경우, 카카오 인증 페이지로 리다이렉트
            httpServletResponse.sendRedirect(
                    "https://kauth.kakao.com/oauth/authorize" +
                            "?client_id=" + oauthConfig.getClientId() +
                            "&redirect_uri=" + oauthConfig.getRedirectUri() +
                            "&response_type=code");

            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Header에서 Access Token 가져오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}