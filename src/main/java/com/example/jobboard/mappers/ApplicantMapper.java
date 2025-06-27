package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.ApplicantResponseDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicantMapper {

    ApplicantMapper INSTANCE = Mappers.getMapper(ApplicantMapper.class);

    ApplicantEntity applicantDtoToApplicant(ApplicantDto applicantDto);

    ApplicantDto applicantToApplicantDto(ApplicantEntity applicantEntity);

    ApplicantResponseDto applicantToResponse(ApplicantEntity applicantEntity);

    ApplicantEntity applicantResponseToApplicant(ApplicantResponseDto applicantResponseDto);

}
