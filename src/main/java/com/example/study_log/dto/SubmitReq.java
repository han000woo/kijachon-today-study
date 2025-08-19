package com.example.study_log.dto;

import com.example.study_log.entity.DifficultyLevel;
import com.example.study_log.entity.StudyLog;
import com.example.study_log.entity.UnderstandingLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmitReq {
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private int difficulty;
    private int understanding;
    private String contents;
    private String memo;
    private String reference;

    public StudyLog convertToEntity(){
        return StudyLog.builder()
                .subject(this.subject)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .category(this.category)
                .difficultyLevel(DifficultyLevel.fromLevel(this.difficulty))
                .understandingLevel(UnderstandingLevel.fromLevel(this.understanding))
                .contents(this.contents)
                .memo(this.memo)
                .reference(this.reference)
                .build();
    }
}
