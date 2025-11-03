package com.rummygp.medical_clinic_proxy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MedicalClinicProxyException extends RuntimeException {
    private final HttpStatus status;

    public MedicalClinicProxyException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
