package com.yadev.spring.integration.database.repository;

import com.yadev.spring.database.entity.Role;
import com.yadev.spring.database.entity.User;
import com.yadev.spring.database.repository.UserRepository;
import com.yadev.spring.dto.UserFilter;
import com.yadev.spring.integration.IntegrationTestBase;
import com.yadev.spring.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void checkBatchUpdate(){
        var users = userRepository.findAll();
        userRepository.updateCompanyAndRole(users);
        System.out.println();
    }
    @Test
    void checkJdbcTemplate() {
        var personalInfos = userRepository.findAllByCompanyIdAndRole(1, Role.USER);
        System.out.println();
    }

    @Test
//    @Commit
    void checkAuditing() {
        var ivan = userRepository.findById(1L).get();
        ivan.setBirthDate(ivan.getBirthDate().plusYears(1));
        userRepository.flush();
        System.out.println();
    }

    @Test
    void checkCustomImplementation() {
        UserFilter filter = new UserFilter(
                null,
                "ov",
                LocalDate.now()

        );
        var users = userRepository.findAllByFilter(filter);
        System.out.println(users);
    }

    @Test
    void checkProjections() {
        var users = userRepository.findAllByCompanyId(1);
        System.out.println();
    }

    @Test
    void checkPageable() {
        var pageable = PageRequest.of(1, 2, Sort.by("id"));
        var page = userRepository.findAllBy(pageable);
        assertThat(page).hasSize(2);

        page.forEach(user -> System.out.println(user.getCompany().getName()));

        while (page.hasNext()) {
            page = userRepository.findAllBy(page.nextPageable());
            page.forEach(user -> System.out.println(user.getCompany().getName()));
        }
    }

    @Test
    void checkTop3() {
        var sortBy = Sort.sort(User.class);
        var sort = sortBy.by(User::getFirstname)
                .and(sortBy.by(User::getLastname));
        var topUsers = userRepository.findTop3ByBirthDateBefore(LocalDate.now(), sort);
        assertThat(topUsers).hasSize(3);
        topUsers.forEach(System.out::println);
    }

    @Test
    void checkFirstTop() {
        var topUser = userRepository.findTopByOrderByIdDesc();
        assertTrue(topUser.isPresent());
        topUser.ifPresent(user -> assertEquals(5L, user.getId()));
    }

    @Test
    void checkUpdate() {
        var sveta = userRepository.getReferenceById(3L);
        assertSame(Role.USER, sveta.getRole());
        var i = userRepository.updateRole(Role.OWNER, 3L, 4L, 5L);
        assertEquals(3, i);

        sveta = userRepository.getReferenceById(3L);
        assertSame(Role.OWNER, sveta.getRole());

    }

    @Test
    void checkQueries() {
        var users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
        System.out.println(users);
    }
}