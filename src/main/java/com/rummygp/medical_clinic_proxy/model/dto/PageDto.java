package com.rummygp.medical_clinic_proxy.model.dto;

import java.util.List;

public record PageDto<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
}
