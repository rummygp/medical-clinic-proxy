package com.rummygp.medical_clinic_proxy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.model.entity.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureWireMock(port = 8881)
public class MedicalClinicClientTest {
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    MedicalClinicClient medicalClinicClient;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnAppointmentClientWhenDataCorrect() throws JsonProcessingException {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .startTime(LocalDateTime.of(3000, 2, 22, 20, 20))
                .endTime(LocalDateTime.of(3000, 2, 22, 21, 20))
                .doctorId(2L)
                .patientId(3L)
                .build();
        Pageable pageable = PageRequest.of(2, 10);
        PageDto<Appointment> page = new PageDto<>(List.of(appointment), pageable.getPageNumber(), pageable.getPageSize(), 1L, 1);

        wireMockServer.stubFor((get("/appointments?doctorId=2&patientId=3&size=10&page=2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page))));

        var result = medicalClinicClient.appointmentDetails(2L, 3L, null, null, null, null, pageable);
        var content = result.content().get(0);

        assertAll(
                () -> assertEquals(1L, content.getId()),
                () -> assertEquals(LocalDateTime.of(3000, 2, 22, 20, 20), content.getStartTime()),
                () -> assertEquals(LocalDateTime.of(3000, 2, 22, 21, 20), content.getEndTime()),
                () -> assertEquals(2L, content.getDoctorId()),
                () -> assertEquals(3L, content.getPatientId())
        );
    }

    @Test
    void shouldReturnEmptyPageWhenNoAppointments() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 5);
        PageDto<Appointment> emptyPage = new PageDto<>(List.of(), pageable.getPageNumber(), pageable.getPageSize(), 0L, 0);

        wireMockServer.stubFor((get("/appointments?doctorId=2&size=5&page=0"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(emptyPage))));

        var result = medicalClinicClient.appointmentDetails(2L, null, null, null, null, null, pageable);

        assertEquals(0, result.content().size());
    }

    @Test
    void shouldBookAppointmentWhenServerReturnsAppointment() throws JsonProcessingException {
        Long appointmentId = 10L;
        Long patientId = 99L;

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .startTime(LocalDateTime.of(2025, 3, 3, 11, 0))
                .endTime(LocalDateTime.of(2025, 3, 3, 11, 30))
                .doctorId(12L)
                .patientId(patientId)
                .build();

        wireMockServer.stubFor((patch(urlEqualTo("/appointments/10/patients/99")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(appointment))));

        Appointment result = medicalClinicClient.book(appointmentId, patientId);

        assertAll(
                () -> assertEquals(10L, result.getId()),
                () -> assertEquals(99L, result.getPatientId()),
                () -> assertEquals(12L, result.getDoctorId())
        );
    }

    @Test
    void shouldCancelAppointmentWhenServerReturnsNoContent() {
        Long id = 33L;

        wireMockServer.stubFor(delete("/appointments/33")
                .willReturn(aResponse()
                        .withStatus(204)));

        medicalClinicClient.cancel(id);

        verify(1, deleteRequestedFor(urlEqualTo("/appointments/33")));
    }

    @Test
    void shouldReturnFreeSlotsWhenRequested() throws JsonProcessingException {
        Long doctorId = 7L;
        LocalDateTime from = LocalDateTime.of(2025, 4, 4, 8, 0);
        LocalDateTime to = LocalDateTime.of(2025, 4, 4, 16, 0);
        Pageable pageable = PageRequest.of(1, 20);

        Appointment appointment = Appointment.builder()
                .id(3L)
                .startTime(LocalDateTime.of(2025, 4, 4, 9, 0))
                .endTime(LocalDateTime.of(2025, 4, 4, 9, 30))
                .doctorId(doctorId)
                .build();

        PageDto<Appointment> page = new PageDto<>(List.of(appointment), pageable.getPageNumber(), pageable.getPageSize(), 1L, 1);

        wireMockServer.stubFor(get(urlPathEqualTo("/appointments"))
                .withQueryParam("doctorId", equalTo("7"))
                .withQueryParam("startingDate", equalTo("2025-04-04T08:00:00"))
                .withQueryParam("endingDate", equalTo("2025-04-04T16:00:00"))
                .withQueryParam("freeSlots", equalTo("true"))
                .withQueryParam("size", equalTo("20"))
                .withQueryParam("page", equalTo("1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page))));

        var result = medicalClinicClient.appointmentDetails(doctorId, null, null, from, to, true, pageable);

        var dto = result.content().get(0);
        assertAll(
                () -> assertEquals(3L, dto.getId()),
                () -> assertEquals(7L, dto.getDoctorId())
        );
    }

    @Test
    void shouldReturnDoctorsPage() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10);
        DoctorDto doctor = new DoctorDto(1L, "John", "Doe", "cardiology", null, List.of(), List.of());
        PageDto<DoctorDto> page = new PageDto<>(List.of(doctor), pageable.getPageNumber(), pageable.getPageSize(), 1L, 1);

        String url = String.format("/doctors?size=%d&page=%d", pageable.getPageSize(), pageable.getPageNumber());

        wireMockServer.stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(page))));

        var result = medicalClinicClient.getDoctors(null, pageable);

        var dto = result.content().get(0);
        assertAll(
                () -> assertEquals(1L, dto.id()),
                () -> assertEquals("John", dto.firstName()),
                () -> assertEquals("Doe", dto.lastName())
        );
    }
}
