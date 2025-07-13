package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.EmployerResponseDto;
import com.example.jobboard.domain.entities.EmployerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CompanyMapper.class)
public interface EmployerMapper {

    EmployerEntity toEntity(EmployerDto employerDto);

    EmployerDto toDto(EmployerEntity employerEntity);

    EmployerResponseDto toResponseDto(EmployerEntity employerEntity);

    EmployerEntity toEntity(EmployerResponseDto employerResponseDto);

}
