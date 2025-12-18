package com.rummygp.medical_clinic_proxy.mapper;

import com.rummygp.medical_clinic_proxy.model.dto.PageDto;
import org.mapstruct.Mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <E, D> PageDto<D> contentMapper(PageDto<E> pageDto, Function<E, D> mapper) {
        var content = pageDto.content().stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageDto<>(
                content,
                pageDto.page(),
                pageDto.size(),
                pageDto.totalElements(),
                pageDto.totalPages()
        );
    }
}
