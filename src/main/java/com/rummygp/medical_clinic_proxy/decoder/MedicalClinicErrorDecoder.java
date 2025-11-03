package com.rummygp.medical_clinic_proxy.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rummygp.medical_clinic_proxy.exception.NotFoundException;
import com.rummygp.medical_clinic_proxy.model.dto.ErrorMessageDto;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;

import java.io.IOException;
import java.io.InputStream;

public class MedicalClinicErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();
    ObjectMapper mapper = new ObjectMapper();

        @Override
        public Exception decode(String methodKey, Response response) {
            ErrorMessageDto message = null;
            try (InputStream bodyIs = response.body()
                    .asInputStream()) {
                message = mapper.readValue(bodyIs, ErrorMessageDto.class);
            } catch (IOException e) {
                return new Exception(e.getMessage());
            }
            return switch (response.status()) {
                case 400 -> new Exception(message.message() != null ? message.message() : "Bad Request");
                case 404 -> new NotFoundException(message.message() != null ? message.message() : "Not found");
                default -> defaultErrorDecoder.decode(methodKey, response);
            };
        }

}

