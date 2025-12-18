package com.rummygp.medical_clinic_proxy.mapper;

import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentResponseDto toDto(Appointment appointment);
}

