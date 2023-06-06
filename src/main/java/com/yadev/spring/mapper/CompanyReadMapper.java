package com.yadev.spring.mapper;

import com.yadev.spring.dto.CompanyReadDto;
import com.yadev.spring.database.entity.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {
    @Override
    public CompanyReadDto map(Company company) {
        return new CompanyReadDto(
                company.getId(),
                company.getName());
    }
}
