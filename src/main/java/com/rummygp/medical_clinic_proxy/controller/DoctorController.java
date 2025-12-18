package com.rummygp.medical_clinic_proxy.controller;

import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.ErrorMessageDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Get page of doctors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DoctorDto.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public PageDto<DoctorDto> getDoctors(@RequestParam String specialization, @ParameterObject Pageable pageable) {
        log.info("GET /doctors called with specialization={}", specialization);
        return doctorService.getDoctors(specialization, pageable);
    }
}
