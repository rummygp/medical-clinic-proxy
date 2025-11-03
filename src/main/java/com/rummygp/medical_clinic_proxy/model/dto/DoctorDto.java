package com.rummygp.medical_clinic_proxy.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DoctorDto(
        Long id,
        String firstName,
        String lastName,
        String specialization,
        UserDto user,
        List<Long> institutionsId,
        List<Long> appointmentsId
) {
}
