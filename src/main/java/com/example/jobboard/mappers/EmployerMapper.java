package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.EmployerResponseDto;
import com.example.jobboard.domain.entities.EmployerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployerMapper {

    EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

    EmployerEntity employerDtoToEmployer(EmployerDto employerDto);

    EmployerDto employerToEmployerDto(EmployerEntity employerEntity);

    EmployerResponseDto employerToResponse(EmployerEntity employerEntity);

    EmployerEntity employerResponseToEmployer(EmployerResponseDto employerResponseDto);

}
