package com.rummygp.medical_clinic_proxy.model.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String email,
        String username
) {
}
