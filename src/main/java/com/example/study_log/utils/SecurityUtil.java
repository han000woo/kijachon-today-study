package com.example.study_log.utils;

import com.example.study_log.exception.CustomException;
import com.example.study_log.exception.ErrorCode;
import com.example.study_log.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static long getCurrentUserId() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        long userId;
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return userId;
    }
}