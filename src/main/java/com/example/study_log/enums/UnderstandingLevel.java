package com.example.study_log.enums;

import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public enum UnderstandingLevel {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    PERFECT(4);

    private final int level;

    UnderstandingLevel(int level) {
        this.level = level;
    }

    public static UnderstandingLevel fromLevel(int level) {
        for (UnderstandingLevel understanding : UnderstandingLevel.values()) {
            if (understanding.level == level) {
                return understanding;
            }
        }
        throw new NoSuchElementException("No understanding level found for value: " + level);
    }
}