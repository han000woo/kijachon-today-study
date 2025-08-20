package com.example.study_log.service;

import com.example.study_log.dto.UserDto;
import com.example.study_log.entity.User;
import com.example.study_log.exception.CustomException;
import com.example.study_log.exception.ErrorCode;
import com.example.study_log.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(UserDto userDto) {
        userRepository.save(userDto.convertUser());
    }

    public User findById(Long id) {

        // 사용자가 없을 경우

        return userRepository.findById(id).orElse(null);
    }
    public UserDto findByRefreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken).orElse(null);

        if (user == null) {
            // 사용자가 없을 경우
            return null;
        }

        return user.convertUserDto();
    }

    public void update(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElse(null);

        if (user == null) {
            // 사용자가 없을 경우
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

        user.update(userDto);
        userRepository.save(user);
    }

    public void updateRefreshToken(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElse(null);

        if (user == null) {
            // 사용자가 없을 경우
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }

        user.updateRefreshToken(userDto.getRefreshToken());
        userRepository.save(user);
    }
}