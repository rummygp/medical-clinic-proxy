package com.rummygp.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends MedicalClinicProxyException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
