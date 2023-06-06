package com.yadev.spring.integration.service;

import com.yadev.spring.database.entity.Role;
import com.yadev.spring.dto.UserCreateEditDto;
import com.yadev.spring.integration.IntegrationTestBase;
import com.yadev.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private final UserService userService;
    private final Long USER_ID = 1L;
    private final Integer COMPANY_ID = 1;

    @Test
    void findAll() {
        var result = userService.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void findById() {
        var maybeUser = userService.findById(USER_ID);
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals("ivan@gmail.com", user.getUsername()));
    }

    @Test
    void create() {
//        var userDto = new UserCreateEditDto(
//                "ihor@gmail.com",
//                LocalDate.of(1999, Month.OCTOBER, 13),
//                "firstname",
//                "lastname",
//                Role.USER,
//                COMPANY_ID
//        );
//        var actualResult = userService.create(userDto);
//        assertEquals("ihor@gmail.com", actualResult.getUsername());
//        assertEquals(LocalDate.of(1999, Month.OCTOBER, 13), actualResult.getBirthDate());
//        assertEquals("firstname", actualResult.getFirstname());
//        assertEquals("lastname", actualResult.getLastname());
//        assertEquals(Role.USER, actualResult.getRole());
    }

    @Test
    void update() {
//        var userDto = new UserCreateEditDto(
//                "ihor@gmail.com",
//                LocalDate.of(1999, Month.OCTOBER, 13),
//                "firstname",
//                "lastname",
//                Role.USER,
//                COMPANY_ID
//        );
//        var actualResult = userService.update(USER_ID, userDto);
//        assertTrue(actualResult.isPresent());
//        actualResult.ifPresent(userReadDto -> {
//            assertEquals("ihor@gmail.com", userReadDto.getUsername());
//            assertEquals(LocalDate.of(1999, Month.OCTOBER, 13), userReadDto.getBirthDate());
//            assertEquals("firstname", userReadDto.getFirstname());
//            assertEquals("lastname", userReadDto.getLastname());
//            assertEquals(Role.USER, userReadDto.getRole());
//        });
    }

    @Test
    void delete() {
        assertFalse(userService.delete(-123409785L));
        assertTrue(userService.delete(USER_ID));
    }
}
