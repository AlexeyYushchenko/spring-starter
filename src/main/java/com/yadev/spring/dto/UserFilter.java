package com.yadev.spring.dto;

import lombok.Data;

import java.time.LocalDate;

public record UserFilter (String firstname,
                          String lastname,
                          LocalDate birthDate) {
}
