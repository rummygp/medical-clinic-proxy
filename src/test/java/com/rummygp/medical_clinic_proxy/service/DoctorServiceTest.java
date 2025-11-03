package com.rummygp.medical_clinic_proxy.service;

import com.rummygp.medical_clinic_proxy.client.DoctorClient;
import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {
    private DoctorClient doctorClient;
    private DoctorService doctorService;

    @BeforeEach
    void setup() {
        this.doctorClient = Mockito.mock(DoctorClient.class);
        this.doctorService = new DoctorService(doctorClient);
    }

    @Test
    void shouldGetDoctorsWithoutSpecialization() {
        Pageable pageable = PageRequest.of(0, 10);
        DoctorDto doctor = new DoctorDto(1L, "John", "Doe", "cardiology", null, List.of(), List.of());
        PageDto<DoctorDto> page = new PageDto<>(List.of(doctor), 0, 10, 1L, 1);

        when(doctorClient.getDoctors(null, pageable)).thenReturn(page);

        PageDto<DoctorDto> result = doctorService.getDoctors(null, pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(doctor.id(), result.content().get(0).id()),
                () -> assertEquals(doctor.firstName(), result.content().get(0).firstName()),
                () -> assertEquals(doctor.lastName(), result.content().get(0).lastName()),
                () -> assertEquals(doctor.specialization(), result.content().get(0).specialization())
        );

        verify(doctorClient, times(1)).getDoctors(null, pageable);
    }

    @Test
    void shouldGetDoctorsWithSpecialization() {
        String specialization = "dentistry";
        Pageable pageable = PageRequest.of(1, 5);
        DoctorDto doctor = new DoctorDto(2L, "Alice", "Smith", specialization, null, List.of(), List.of());
        PageDto<DoctorDto> page = new PageDto<>(List.of(doctor), 1, 5, 1L, 1);

        when(doctorClient.getDoctors(specialization, pageable)).thenReturn(page);

        PageDto<DoctorDto> result = doctorService.getDoctors(specialization, pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(doctor.id(), result.content().get(0).id()),
                () -> assertEquals(doctor.firstName(), result.content().get(0).firstName()),
                () -> assertEquals(doctor.lastName(), result.content().get(0).lastName()),
                () -> assertEquals(doctor.specialization(), result.content().get(0).specialization())
        );

        verify(doctorClient, times(1)).getDoctors(specialization, pageable);
    }
}
