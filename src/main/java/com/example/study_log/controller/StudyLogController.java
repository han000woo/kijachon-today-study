package com.example.study_log.controller;

import com.example.study_log.dto.SubmitReq;
import com.example.study_log.dto.SubmitRes;
import com.example.study_log.service.StudyLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyLogController {

    private final StudyLogService studyLogService;

    @PostMapping("/submit-study")
    public SubmitRes submitStudy(SubmitReq submitReq){

        SubmitRes submitRes = studyLogService.saveLog(submitReq);

        return submitRes;
    }
}
