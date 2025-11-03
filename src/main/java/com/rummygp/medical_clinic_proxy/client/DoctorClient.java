package com.rummygp.medical_clinic_proxy.client;

import com.rummygp.medical_clinic_proxy.config.MedicalClinicConfig;
import com.rummygp.medical_clinic_proxy.model.dto.DoctorDto;
import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "medical-clinic", contextId = "doctorClient", url = "${medical-clinic.url}", configuration = MedicalClinicConfig.class, fallbackFactory = DoctorClientFallback.class)
public interface DoctorClient {

    @GetMapping("/doctors")
    PageDto<DoctorDto> getDoctors(@RequestParam(required = false) String specialization,
                                  @ParameterObject Pageable pageable);
}
