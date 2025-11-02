package com.rummygp.medical_clinic_proxy.client;

import com.rummygp.medical_clinic_proxy.config.MedicalClinicConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "medical-clinic", contextId = "doctorClient", url = "http://localhost:8080", configuration = MedicalClinicConfig.class)
public interface DoctorClient {
}
