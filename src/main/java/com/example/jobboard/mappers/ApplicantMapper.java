package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.ApplicantResponseDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicantMapper {

    ApplicantEntity applicantDtoToApplicant(ApplicantDto applicantDto);

    ApplicantDto applicantToApplicantDto(ApplicantEntity applicantEntity);

    ApplicantResponseDto applicantToResponse(ApplicantEntity applicantEntity);

    ApplicantEntity applicantResponseToApplicant(ApplicantResponseDto applicantResponseDto);

}
