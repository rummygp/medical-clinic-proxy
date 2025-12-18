package com.rummygp.medical_clinic_proxy.service;

import com.rummygp.medical_clinic_proxy.client.MedicalClinicClient;
import com.rummygp.medical_clinic_proxy.mapper.AppointmentMapper;
import com.rummygp.medical_clinic_proxy.mapper.PageMapper;
import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final MedicalClinicClient medicalClinicClient;
    private final AppointmentMapper appointmentMapper;
    private final PageMapper pageMapper;

    public PageDto<AppointmentResponseDto> find(Long patientId, Long doctorId, String specialization, LocalDateTime startingDate, LocalDateTime endingDate, Pageable pageable) {
        log.debug("Fetching appointments for patientId='{}', doctorId='{}', specialization='{}', startingDate='{}', endingDate='{}', pageable={}", patientId, doctorId, specialization, startingDate, endingDate, pageable);
        return pageMapper.contentMapper(medicalClinicClient.appointmentDetails(doctorId, patientId, specialization, startingDate, endingDate, null, pageable), appointmentMapper::toDto);
    }

    public AppointmentResponseDto book(Long appointmentId, Long patientId) {
        log.debug("Booking appointment appointmentId='{}' for patientId='{}'", appointmentId, patientId);
        return appointmentMapper.toDto(medicalClinicClient.book(appointmentId,patientId));
    }

    public PageDto<AppointmentResponseDto> findAvailable(Long doctorId, String specialization,
                                                         LocalDateTime startingDate, LocalDateTime endingDate, Pageable pageable) {
        log.debug("Fetching available appointments for doctorId='{}', specialization='{}', startingDate='{}', endingDate='{}', pageable={}", doctorId, specialization, startingDate, endingDate, pageable);
        return pageMapper.contentMapper(medicalClinicClient.appointmentDetails(doctorId, null, specialization, startingDate, endingDate, true, pageable), appointmentMapper::toDto);
    }

    public void cancel(Long id) {
        log.debug("Cancelling appointment id='{}'", id);
        medicalClinicClient.cancel(id);
    }
}
