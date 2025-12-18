package com.rummygp.medical_clinic_proxy.service;

import com.rummygp.medical_clinic_proxy.client.MedicalClinicClient;
import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {
    private final MedicalClinicClient medicalClinicClient;

    public PageDto<DoctorDto> getDoctors(String specialization, Pageable pageable) {
        log.debug("Fetching doctors with specialization='{}', pageable={}", specialization, pageable);
        return medicalClinicClient.getDoctors(specialization, pageable);
    }
}
