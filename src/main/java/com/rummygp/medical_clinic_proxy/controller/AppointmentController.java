package com.rummygp.medical_clinic_proxy.controller;

import com.rummygp.medical_clinic_proxy.model.dto.AppointmentResponseDto;
import com.rummygp.medical_clinic_proxy.model.dto.ErrorMessageDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import com.rummygp.medical_clinic_proxy.service.AppointmentService;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Get page of appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Doctor or patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping
    public PageDto<AppointmentResponseDto> find(@RequestParam(required = false) Long patientId,
                                                @RequestParam(required = false) Long doctorId,
                                                @RequestParam(required = false) String specialization,
                                                @RequestParam(required = false) LocalDateTime startingDate,
                                                @RequestParam(required = false) LocalDateTime endingDate,
                                                @ParameterObject Pageable pageable) {
        log.info("GET /appointments called");
        return appointmentService.find(patientId, doctorId, specialization, startingDate, endingDate, pageable);
    }

    @Operation(summary = "Book the appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The appointment has been booked successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input data.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Id not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "409", description = "The appointment is already booked",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @PatchMapping("/{appointmentId}/patients/{patientId}")
    public AppointmentResponseDto book(@PathVariable Long appointmentId, @PathVariable Long patientId) {
        log.info("PATCH /appointments/{}/patients/{} called", appointmentId, patientId);
        return appointmentService.book(appointmentId, patientId);
    }

    @Operation(summary = "Get page of available appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments page returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentResponseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Doctor or patient not found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @GetMapping("/available")
    public PageDto<AppointmentResponseDto> findAvailable(@RequestParam(required = false) Long doctorId,
                                                         @RequestParam(required = false) String specialization,
                                                         @RequestParam(required = false) LocalDateTime startingDate,
                                                         @RequestParam(required = false) LocalDateTime endingDate,
                                                         @ParameterObject Pageable pageable) {
        log.info("GET /appointments/available called");
        return appointmentService.findAvailable(doctorId, specialization, startingDate, endingDate, pageable);
    }

    @Operation(summary = "Delete existing appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient has been deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessageDto.class))})})
    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        log.info("DELETE /appointments/{} called", id);
        appointmentService.cancel(id);
    }
}
