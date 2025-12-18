package com.rummygp.medical_clinic_proxy.config;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }
}
