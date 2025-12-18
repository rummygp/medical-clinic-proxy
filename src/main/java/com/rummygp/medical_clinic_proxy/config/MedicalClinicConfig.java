package com.rummygp.medical_clinic_proxy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummygp.medical_clinic_proxy.decoder.MedicalClinicErrorDecoder;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MedicalClinicConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(3L), 5);
    }

    @Bean
    public ErrorDecoder errorDecoder(ObjectMapper mapper) {
        return new MedicalClinicErrorDecoder(mapper);
    }
}
