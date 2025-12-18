package com.rummygp.medical_clinic_proxy.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentResponseDto(Long id, LocalDateTime startTime, LocalDateTime endTime, Long doctorId, Long patientId) {
}
