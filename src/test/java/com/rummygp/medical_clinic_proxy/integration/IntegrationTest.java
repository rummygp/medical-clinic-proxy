package com.rummygp.medical_clinic_proxy.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.model.entity.Appointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@AutoConfigureWireMock(port = 8881)
public class IntegrationTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnPageOfAppointmentWhenDataCorrect() throws JsonProcessingException {
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

        ResponseEntity<String> response = testRestTemplate.getForEntity("http://localhost:8081/appointments?doctorId=2&patientId=3&size=10&page=2", String.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode())
        );
    }

    @Test
    void shouldBookAppointmentWhenDataCorrect() throws JsonProcessingException {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .startTime(LocalDateTime.of(3000, 2, 22, 20, 20))
                .endTime(LocalDateTime.of(3000, 2, 22, 21, 20))
                .doctorId(3L)
                .patientId(2L)
                .build();
        Long patientId = 2L;

        wireMockServer.stubFor((patch(urlEqualTo("/appointments/1/patients/2")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(appointment))));

        ResponseEntity<AppointmentResponseDto> response = testRestTemplate.exchange(
                "http://localhost:8081/appointments/{appointmentId}/patients/{patientId}",
                HttpMethod.PATCH,
                new HttpEntity<>(null),
                AppointmentResponseDto.class,
                "1", "2"
        );

        assertAll(
                () -> assertEquals(1L, response.getBody().id()),
                () -> assertEquals(3L, response.getBody().doctorId())
        );
    }
}
