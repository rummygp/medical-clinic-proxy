package com.rummygp.medical_clinic_proxy.client;

import com.rummygp.medical_clinic_proxy.config.MedicalClinicConfig;
import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.model.entity.Appointment;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@FeignClient(name = "medical-clinic", configuration = MedicalClinicConfig.class, fallbackFactory = MedicalClientClientFallback.class)
public interface MedicalClinicClient {

    @GetMapping("/appointments")
    PageDto<Appointment> appointmentDetails(@RequestParam(required = false) Long doctorId,
                                             @RequestParam(required = false) Long patientId,
                                             @RequestParam(required = false) String specialization,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startingDate,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endingDate,
                                             @RequestParam(required = false) Boolean freeSlots,
                                             @ParameterObject Pageable pageable);

    @PatchMapping("appointments/{appointmentId}/patients/{patientId}")
    Appointment book(@PathVariable Long appointmentId, @PathVariable Long patientId);

    @DeleteMapping("appointments/{id}")
    void cancel(@PathVariable Long id);

    @GetMapping("/doctors")
    PageDto<DoctorDto> getDoctors(@RequestParam(required = false) String specialization,
                                  @ParameterObject Pageable pageable);
}
