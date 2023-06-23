package com.yadev.spring.mapper;

import com.yadev.spring.database.entity.User;
import com.yadev.spring.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto map(User user) {
        var companyReadDto = Optional.of(user.getCompany())
                .map(companyReadMapper::map)
                .orElse(null);
        return new UserReadDto(
                user.getId(),
                user.getUsername(),
                user.getImage(),
                user.getBirthDate(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole(),
                companyReadDto
        );
    }
}