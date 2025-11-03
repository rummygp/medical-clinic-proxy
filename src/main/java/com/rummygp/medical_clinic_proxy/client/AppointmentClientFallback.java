package com.rummygp.medical_clinic_proxy.client;

import com.rummygp.medical_clinic_proxy.exception.AppointmentClientUnavailableException;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.model.entity.Appointment;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class AppointmentClientFallback implements FallbackFactory<AppointmentClient> {
    @Override
    public AppointmentClient create(Throwable cause) {
        return new AppointmentClient() {
            @Override
            public PageDto<Appointment> appointmentDetails(Long doctorId, Long patientId, String specialization, LocalDate date, LocalDateTime startingDate, LocalDateTime endingDate, Pageable pageable) {
                return new PageDto<>(Collections.emptyList(), pageable.getPageNumber(), pageable.getPageSize(), 0L, 0);
            }

            @Override
            public Appointment book(Long appointmentId, Long patientId) {
                throw new AppointmentClientUnavailableException("Appointment booking is currently unavailable");
            }

            @Override
            public void cancel(Long id) {
                throw new AppointmentClientUnavailableException("Appointment cancel is currently unavailable");
            }
        };
    }
}
