package com.example.study_log.entity;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum DifficultyLevel {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int level;

    DifficultyLevel(int level) {
        this.level = level;
    }

    // 숫자로 Enum을 찾는 정적 메서드
    public static DifficultyLevel fromLevel(int level) {
        for (DifficultyLevel difficulty : DifficultyLevel.values()) {
            if (difficulty.level == level) {
                return difficulty;
            }
        }
        // 해당하는 Enum이 없을 경우 예외 발생
        throw new NoSuchElementException("No difficulty level found for value: " + level);
    }
}