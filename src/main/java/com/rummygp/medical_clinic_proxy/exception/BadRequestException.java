package com.rummygp.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends MedicalClinicProxyException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
