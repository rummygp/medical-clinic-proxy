package com.rummygp.medical_clinic_proxy.service;

import com.rummygp.medical_clinic_proxy.client.AppointmentClient;
import com.rummygp.medical_clinic_proxy.mapper.AppointmentMapper;
import com.rummygp.medical_clinic_proxy.mapper.PageMapper;
import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final AppointmentClient appointmentClient;
    private final AppointmentMapper appointmentMapper;
    private final PageMapper pageMapper;

    public PageDto<AppointmentResponseDto> findForPatient(Long patientId, String specialization, LocalDateTime startingDate, LocalDateTime endingDate, Pageable pageable) {
        log.debug("Fetching appointments for patientId='{}', specialization='{}', startingDate='{}', endingDate='{}', pageable={}", patientId, specialization, startingDate, endingDate, pageable);
        return pageMapper.toDto(appointmentClient.appointmentDetails(null, patientId, specialization, null, startingDate, endingDate, pageable), appointmentMapper::toDto);
    }

    public PageDto<AppointmentResponseDto> findForDoctor(Long doctorId, Pageable pageable) {
        log.debug("Fetching appointments for doctorId='{}', pageable={}", doctorId, pageable);
        return pageMapper.toDto(appointmentClient.appointmentDetails(doctorId, null, null, null, null, null, pageable), appointmentMapper::toDto);
    }

    public AppointmentResponseDto book(Long appointmentId, Long patientId) {
        log.debug("Booking appointment appointmentId='{}' for patientId='{}'", appointmentId, patientId);
        return appointmentMapper.toDto(appointmentClient.book(appointmentId,patientId));
    }

    public PageDto<AppointmentResponseDto> findAvailable(Long doctorId, String specialization, LocalDate date,
                                                         LocalDateTime startingDate, LocalDateTime endingDate, Pageable pageable) {
        log.debug("Fetching available appointments for doctorId='{}', specialization='{}', date='{}', startingDate='{}', endingDate='{}', pageable={}", doctorId, specialization, date, startingDate, endingDate, pageable);
        return pageMapper.toDto(appointmentClient.appointmentDetails(doctorId, null, specialization, date, startingDate, endingDate, pageable), appointmentMapper::toDto);
    }

    public void cancel(Long id) {
        log.debug("Cancelling appointment id='{}'", id);
        appointmentClient.cancel(id);
    }
}
