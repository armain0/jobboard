package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.domain.entities.ApplicationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ApplicantMapper.class, JobMapper.class})
public interface ApplicationMapper {

    ApplicationEntity toEntity(ApplicationDto applicationDto);

    ApplicationDto toDto(ApplicationEntity applicationEntity);

}
