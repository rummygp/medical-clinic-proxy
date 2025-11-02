package com.rummygp.medical_clinic_proxy.model.dto;

import java.time.LocalDateTime;

public record AppointmentResponseDto(Long id, LocalDateTime startTime, LocalDateTime endTime, Long doctorId, Long patientId) {
}
