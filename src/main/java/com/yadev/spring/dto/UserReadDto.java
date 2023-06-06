package com.yadev.spring.dto;

import com.yadev.spring.database.entity.Role;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Value
public class UserReadDto {
    Long id;
    String username;
    String image;
    LocalDate birthDate;
    String firstname;
    String lastname;
    Role role;
    CompanyReadDto company;
}
