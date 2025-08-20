package com.example.study_log.controller;

import com.example.study_log.dto.UserDto;
import com.example.study_log.entity.User;
import com.example.study_log.exception.CustomException;
import com.example.study_log.exception.ErrorCode;
import com.example.study_log.service.UserService;
import com.example.study_log.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // 유저정보 조회 API
    @GetMapping("/info")
    public UserDto info() {
        final long userId = SecurityUtil.getCurrentUserId();
        User user = userService.findById(userId);
        if(user == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }
        return user.convertUserDto();
    }
}