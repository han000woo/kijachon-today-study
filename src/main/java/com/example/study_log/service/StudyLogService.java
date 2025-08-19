package com.example.study_log.service;

import com.example.study_log.dto.SubmitReq;
import com.example.study_log.dto.SubmitRes;
import com.example.study_log.entity.StudyLog;
import com.example.study_log.repository.StudyLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;

    // 랜덤 메시지 목록
    private static final List<String> RANDOM_MESSAGES = List.of(
            "잘했어요!",
            "앞으로도 파이팅!",
            "오늘도 수고했어요!",
            "대단해요!",
            "꾸준함이 힘이에요!",
            "멋진 하루네요!"
    );

    public SubmitRes saveLog(SubmitReq submitReq) {
        try {
            StudyLog studyLog = submitReq.convertToEntity();
            studyLogRepository.save(studyLog);

            return buildResponse(200, getRandomMessage());

        } catch (DataAccessException e) {
            log.error("DB 저장 에러 발생", e);
            return buildResponse(500, "DATABASE_SAVE_FAILED");

        } catch (Exception e) {
            log.error("예상치 못한 오류 발생", e);
            return buildResponse(500, "INTERNAL_SERVER_ERROR");
        }
    }

    private String getRandomMessage() {
        int index = ThreadLocalRandom.current().nextInt(RANDOM_MESSAGES.size());
        return RANDOM_MESSAGES.get(index);
    }

    private SubmitRes buildResponse(int statusCode, String message) {
        return SubmitRes.builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}
