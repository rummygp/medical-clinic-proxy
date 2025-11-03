package com.rummygp.medical_clinic_proxy.service;

import com.rummygp.medical_clinic_proxy.client.AppointmentClient;
import com.rummygp.medical_clinic_proxy.mapper.AppointmentMapper;
import com.rummygp.medical_clinic_proxy.mapper.PageMapper;
import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.model.entity.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppointmentServiceTest {
    private AppointmentClient appointmentClient;
    private AppointmentMapper appointmentMapper;
    private PageMapper pageMapper;
    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        this.appointmentClient = Mockito.mock(AppointmentClient.class);
        this.appointmentMapper = Mappers.getMapper(AppointmentMapper.class);
        this.pageMapper = Mappers.getMapper(PageMapper.class);
        this.appointmentService = new AppointmentService(appointmentClient, appointmentMapper, pageMapper);
    }

    @Test
    void shouldFindForPatient() {
        Long patientId = 42L;
        String specialization = "cardiology";
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        Pageable pageable = PageRequest.of(0, 10);

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStartTime(LocalDateTime.of(2025, 1, 1, 9, 0));
        appointment.setEndTime(LocalDateTime.of(2025, 1, 1, 9, 30));
        appointment.setDoctorId(7L);
        appointment.setPatientId(patientId);

        PageDto<Appointment> page = new PageDto<>(List.of(appointment), 0, 10, 1L, 1);

        when(appointmentClient.appointmentDetails(null, patientId, specialization, null, from, to, pageable)).thenReturn(page);

        PageDto<AppointmentResponseDto> result = appointmentService.findForPatient(patientId, specialization, from, to, pageable);
        AppointmentResponseDto dto = result.content().get(0);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(appointment.getId(), dto.id()),
                () -> assertEquals(appointment.getStartTime(), dto.startTime()),
                () -> assertEquals(appointment.getEndTime(), dto.endTime()),
                () -> assertEquals(appointment.getDoctorId(), dto.doctorId()),
                () -> assertEquals(appointment.getPatientId(), dto.patientId())
        );
    }

    @Test
    void shouldFindForDoctor() {
        Long doctorId = 7L;
        Pageable pageable = PageRequest.of(0, 5);

        Appointment appointment = new Appointment();
        appointment.setId(2L);
        appointment.setStartTime(LocalDateTime.of(2025, 2, 2, 10, 0));
        appointment.setEndTime(LocalDateTime.of(2025, 2, 2, 10, 30));
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(55L);

        PageDto<Appointment> page = new PageDto<>(List.of(appointment), 0, 5, 1L, 1);

        when(appointmentClient.appointmentDetails(doctorId, null, null, null, null, null, pageable)).thenReturn(page);

        PageDto<AppointmentResponseDto> result = appointmentService.findForDoctor(doctorId, pageable);
        AppointmentResponseDto dto = result.content().get(0);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(appointment.getId(), dto.id()),
                () -> assertEquals(appointment.getDoctorId(), dto.doctorId()),
                () -> assertEquals(appointment.getPatientId(), dto.patientId())
        );
    }

    @Test
    void shouldBookAppointment() {
        Long appointmentId = 10L;
        Long patientId = 99L;

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStartTime(LocalDateTime.of(2025, 3, 3, 11, 0));
        appointment.setEndTime(LocalDateTime.of(2025, 3, 3, 11, 30));
        appointment.setDoctorId(12L);
        appointment.setPatientId(patientId);

        when(appointmentClient.book(appointmentId, patientId)).thenReturn(appointment);
        AppointmentResponseDto dto = appointmentService.book(appointmentId, patientId);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(appointment.getId(), dto.id()),
                () -> assertEquals(appointment.getPatientId(), dto.patientId()),
                () -> assertEquals(appointment.getDoctorId(), dto.doctorId())
        );
    }

    @Test
    void shouldFindAvailable() {
        Long doctorId = 7L;
        String specialization = "dentistry";
        LocalDate date = LocalDate.of(2025, 4, 4);
        LocalDateTime from = LocalDateTime.of(2025, 4, 4, 8, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 4, 16, 0);
        Pageable pageable = PageRequest.of(1, 20);

        Appointment appointment = new Appointment();
        appointment.setId(3L);
        appointment.setStartTime(LocalDateTime.of(2025, 4, 4, 9, 0));
        appointment.setEndTime(LocalDateTime.of(2025, 4, 4, 9, 30));
        appointment.setDoctorId(doctorId);

        PageDto<Appointment> page = new PageDto<>(List.of(appointment), 1, 20, 1L, 1);

        when(appointmentClient.appointmentDetails(doctorId, null, specialization, date, from, to, pageable)).thenReturn(page);

        PageDto<AppointmentResponseDto> result = appointmentService.findAvailable(doctorId, specialization, date, from, to, pageable);
        AppointmentResponseDto dto = result.content().get(0);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(appointment.getId(), dto.id()),
                () -> assertEquals(appointment.getDoctorId(), dto.doctorId())
        );
    }

    @Test
    void shouldCancelAppointment() {
        Long id = 77L;

        doNothing().when(appointmentClient).cancel(id);

        appointmentService.cancel(id);

        assertAll(() -> verify(appointmentClient, times(1)).cancel(id));
    }

}
