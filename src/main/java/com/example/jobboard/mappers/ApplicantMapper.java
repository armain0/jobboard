package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicantMapper {

    ApplicantEntity toEntity(ApplicantDto applicantDto);

    ApplicantDto toDto(ApplicantEntity applicantEntity);

}
