package com.rummygp.medical_clinic_proxy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.model.dto.UserDto;
import com.rummygp.medical_clinic_proxy.service.DoctorService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {
    @MockitoBean
    private DoctorService doctorService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPagedDoctorWithSpecializationDtosWhenDataCorrect() throws Exception {
        String specialization = "Kardiologia";
        UserDto userDto1 = UserDto.builder().id(1L).build();
        UserDto userDto2 = UserDto.builder().id(2L).build();
        DoctorDto doctor1 = DoctorDto.builder()
                .id(3L)
                .firstName("doctorFirstName1")
                .lastName("doctorLastName1")
                .specialization(specialization)
                .user(userDto1)
                .institutionsId(new ArrayList<>())
                .appointmentsId(new ArrayList<>())
                .build();
        DoctorDto doctor2 = DoctorDto.builder()
                .id(4L)
                .firstName("doctorFirstName2")
                .lastName("doctorLastName2")
                .specialization(specialization)
                .user(userDto2)
                .institutionsId(new ArrayList<>())
                .appointmentsId(new ArrayList<>())
                .build();
        Pageable pageable = PageRequest.of(0, 2);
        PageDto<DoctorDto> page = new PageDto<>(List.of(doctor1, doctor2), pageable.getPageNumber(), pageable.getPageSize(), 2, 2);

        when(doctorService.getDoctors(specialization, pageable)).thenReturn(page);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/doctors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "2")
                                .param("specialization", "Kardiologia")
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content", hasSize(2)),
                        jsonPath("$.content[0].id").value(3L),
                        jsonPath("$.content[0].firstName").value("doctorFirstName1"),
                        jsonPath("$.content[0].lastName").value("doctorLastName1"),
                        jsonPath("$.content[0].specialization").value("Kardiologia"),
                        jsonPath("$.content[0].user.id").value(1L),
                        jsonPath("$.content[0].appointmentsId", hasSize(0)),
                        jsonPath("$.content[0].institutionsId", hasSize(0))
                );
    }
}


