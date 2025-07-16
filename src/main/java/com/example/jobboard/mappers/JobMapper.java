package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.entities.JobEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EmployerMapper.class, CompanyMapper.class})
public interface JobMapper {

    @Mapping(target = "applications", ignore = true)
    @Mapping(target = "id", ignore = true)
    JobEntity toEntity(JobDto jobDto);

    @Mapping(target = "employer.password", ignore = true)
    JobDto toDto(JobEntity jobEntity);

}
