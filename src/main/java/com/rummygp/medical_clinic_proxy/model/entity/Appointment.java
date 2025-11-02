package com.rummygp.medical_clinic_proxy.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Appointment {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long doctorId;
    private Long patientId;
}
