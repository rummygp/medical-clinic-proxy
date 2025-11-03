package com.rummygp.medical_clinic_proxy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {
    @MockitoBean
    private AppointmentService appointmentService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFindForPatient() throws Exception {
        Long patientId = 42L;
        String specialization = "cardiology";
        String starting = "2025-01-01T09:00:00";
        String ending = "2025-01-01T17:00:00";
        Pageable pageable = PageRequest.of(0, 10);

        AppointmentResponseDto dto = new AppointmentResponseDto(1L, LocalDateTime.parse("2025-01-01T09:00:00"), LocalDateTime.parse("2025-01-01T09:30:00"), 7L, patientId);
        PageDto<AppointmentResponseDto> page = new PageDto<>(List.of(dto), pageable.getPageNumber(), pageable.getPageSize(), 1L, 1);

        when(appointmentService.findForPatient(patientId, specialization, LocalDateTime.parse(starting), LocalDateTime.parse(ending), pageable)).thenReturn(page);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/appointments/patient/{id}", patientId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("specialization", specialization)
                                .param("startingDate", starting)
                                .param("endingDate", ending)
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(1)),
                        jsonPath("$.content[0].id").value(1),
                        jsonPath("$.content[0].doctorId").value(7),
                        jsonPath("$.content[0].patientId").value(42)
                );
    }

    @Test
    void shouldFindForDoctor() throws Exception {
        Long doctorId = 7L;
        Pageable pageable = PageRequest.of(0, 5);

        AppointmentResponseDto dto = new AppointmentResponseDto(2L, LocalDateTime.parse("2025-02-02T10:00:00"), LocalDateTime.parse("2025-02-02T10:30:00"), doctorId, 55L);
        PageDto<AppointmentResponseDto> page = new PageDto<>(List.of(dto), pageable.getPageNumber(), pageable.getPageSize(), 1L, 1);

        when(appointmentService.findForDoctor(doctorId, pageable)).thenReturn(page);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/appointments/doctor/{id}", doctorId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "5")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(1)),
                        jsonPath("$.content[0].id").value(2),
                        jsonPath("$.content[0].doctorId").value(7),
                        jsonPath("$.content[0].patientId").value(55)
                );
    }

    @Test
    void shouldBook() throws Exception {
        Long appointmentId = 10L;
        Long patientId = 99L;

        AppointmentResponseDto dto = new AppointmentResponseDto(appointmentId, LocalDateTime.parse("2025-03-03T11:00:00"), LocalDateTime.parse("2025-03-03T11:30:00"), 12L, patientId);

        when(appointmentService.book(appointmentId, patientId)).thenReturn(dto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/appointments/{appointmentId}/patients/{patientId}", appointmentId, patientId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(10),
                        jsonPath("$.doctorId").value(12),
                        jsonPath("$.patientId").value(99)
                );
    }

    @Test
    void shouldFindAvailable() throws Exception {
        Long doctorId = 7L;
        String specialization = "dentistry";
        String date = "2025-04-04";
        String starting = "2025-04-04T08:00:00";
        String ending = "2025-04-04T16:00:00";
        Pageable pageable = PageRequest.of(1, 20);

        AppointmentResponseDto dto = new AppointmentResponseDto(3L, LocalDateTime.parse("2025-04-04T09:00:00"), LocalDateTime.parse("2025-04-04T09:30:00"), doctorId, null);
        PageDto<AppointmentResponseDto> page = new PageDto<>(List.of(dto), pageable.getPageNumber(), pageable.getPageSize(), 1L, 1);

        when(appointmentService.findAvailable(doctorId, specialization, LocalDate.parse(date), LocalDateTime.parse(starting), LocalDateTime.parse(ending), pageable)).thenReturn(page);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/appointments/available")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("doctorId", String.valueOf(doctorId))
                                .param("specialization", specialization)
                                .param("date", date)
                                .param("startingDate", starting)
                                .param("endingDate", ending)
                                .param("page", "1")
                                .param("size", "20")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(1)),
                        jsonPath("$.content[0].id").value(3),
                        jsonPath("$.content[0].doctorId").value(7)
                );
    }

    @Test
    void shouldCancel() throws Exception {
        Long id = 77L;
        doNothing().when(appointmentService).cancel(id);

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/appointments/{id}", id)
                )
                .andExpect(status().isOk());
    }
}

