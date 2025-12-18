package com.rummygp.medical_clinic_proxy.exception;

import org.springframework.http.HttpStatus;

public class AppointmentClientUnavailableException extends MedicalClinicProxyException {
    public AppointmentClientUnavailableException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
