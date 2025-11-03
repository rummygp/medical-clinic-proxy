package com.rummygp.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends MedicalClinicProxyException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
