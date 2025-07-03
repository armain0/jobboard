package com.example.jobboard.mappers;

import com.example.jobboard.domain.dto.CompanyDto;
import com.example.jobboard.domain.entities.CompanyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyEntity companyDtoToCompany(CompanyDto companyDto);

    CompanyDto companyToCompanyDto(CompanyEntity companyEntity);

}
