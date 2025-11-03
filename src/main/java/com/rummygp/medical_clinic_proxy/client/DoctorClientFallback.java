package com.rummygp.medical_clinic_proxy.client;

import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

public class DoctorClientFallback implements FallbackFactory<DoctorClient> {

    @Override
    public DoctorClient create(Throwable cause) {
        return new DoctorClient() {

            @Override
            public PageDto<DoctorDto> getDoctors(String specialization, Pageable pageable) {
                return new PageDto<>(Collections.emptyList(), pageable.getPageNumber(), pageable.getPageSize(), 0L, 0);
            }
        };
    }
}
