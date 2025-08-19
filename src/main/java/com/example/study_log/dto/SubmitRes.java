package com.example.study_log.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitRes {
    private String message;
    private int statusCode;
}
