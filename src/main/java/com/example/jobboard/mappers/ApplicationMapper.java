package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.domain.entities.ApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ApplicantMapper.class, JobMapper.class, CompanyMapper.class})
public interface ApplicationMapper {

    ApplicationEntity toEntity(ApplicationDto applicationDto);

    @Mapping(target = "applicant.password", ignore = true)
    @Mapping(target = "job.employer.password", ignore = true)
    ApplicationDto toDto(ApplicationEntity applicationEntity);

}
