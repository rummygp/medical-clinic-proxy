package com.rummygp.medical_clinic_proxy.controller;

import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/patient/{id}")
    public PageDto<AppointmentResponseDto> findForPatient(@PathVariable Long id,
                                                          @RequestParam(required = false) String specialization,
                                                          @RequestParam(required = false) LocalDateTime startingDate,
                                                          @RequestParam(required = false) LocalDateTime endingDate,
                                                          @ParameterObject Pageable pageable) {
        log.info("GET /appointments/patient/{} called", id);
        return appointmentService.findForPatient(id, specialization, startingDate, endingDate, pageable);
    }

    @GetMapping("/doctor/{id}")
    public PageDto<AppointmentResponseDto> findForDoctor(@PathVariable Long id,
                                                         @ParameterObject Pageable pageable) {
        log.info("GET /appointments/doctor/{} called", id);
        return appointmentService.findForDoctor(id, pageable);
    }

    @PatchMapping("/{appointmentId}/patients/{patientId}")
    public AppointmentResponseDto book(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        log.info("PATCH /appointments/{}/patients/{} called", appointmentId, patientId);
        return appointmentService.book(appointmentId, patientId);
    }

    @GetMapping("/available")
    public PageDto<AppointmentResponseDto> findAvailable(@RequestParam(required = false) Long doctorId,
                                                         @RequestParam(required = false) String specialization,
                                                         @RequestParam(required = false) LocalDate date,
                                                         @RequestParam(required = false) LocalDateTime startingDate,
                                                         @RequestParam(required = false) LocalDateTime endingDate,
                                                         @ParameterObject Pageable pageable) {
        log.info("GET /appointments/available called");
        return appointmentService.findAvailable(doctorId, specialization, date, startingDate, endingDate, pageable);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        log.info("DELETE /appointments/{} called", id);
        appointmentService.cancel(id);
    }
}
