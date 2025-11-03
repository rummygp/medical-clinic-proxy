package com.rummygp.medical_clinic_proxy.exception;

import com.rummygp.medical_clinic_proxy.model.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class MedicalClinicProxyRestExceptionHandler {

    @ExceptionHandler(value = MedicalClinicProxyException.class)
    protected ResponseEntity<ErrorMessageDto> handleMedicalClinicException(MedicalClinicProxyException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                ErrorMessageDto.builder()
                        .createdAt(LocalDate.now())
                        .message(ex.getMessage())
                        .error(ex.getStatus().getReasonPhrase())
                        .status(ex.getStatus().value())
                        .build()
        );
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorMessageDto handleGlobalException(Exception ex) {
        return ErrorMessageDto.builder()
                .createdAt(LocalDate.now())
                .message(ex.getMessage())
                .error("Unknown Error")
                .status(500)
                .build();
    }
}
