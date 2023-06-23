package com.yadev.spring.database.repository;

import com.yadev.spring.database.entity.Role;
import com.yadev.spring.database.entity.User;
import com.yadev.spring.dto.PersonalInfo2;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        FilterUserRepository,
        RevisionRepository<User, Long, Integer>,
        QuerydslPredicateExecutor<User> {

    //    List<PersonalInfo> findAllByCompanyId(Integer companyId);
//    List<PersonalInfo2> findAllByCompanyId(Integer companyId);
//    <T> List<T> findAllByCompanyId(Integer companyId, Class<T> clazz);
    @Query(value = "SELECT firstname, lastname, birth_date birthDate " +
            "FROM users " +
            "WHERE company_id = :companyId",
            nativeQuery = true)
    List<PersonalInfo2> findAllByCompanyId(Integer companyId);

    //    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH)
//    @Query(value = "select u from User u join fetch u.company c",
//            countQuery = "select count(distinct u.firstname) from User u")
//    @EntityGraph("user-company")
//    @EntityGraph("User.company")
    @EntityGraph(attributePaths = {"company"})
    @Query(value = "select u from User u",
            countQuery = "select count(distinct u.firstname) from User u")
    Page<User> findAllBy(Pageable pageable);

    Optional<User> findTopByOrderByIdDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<User> findTop3ByBirthDateBefore(LocalDate birthDate, Sort sort);

    @Query("select u from User u " +
            "where u.firstname like %:firstname% AND u.lastname like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);

    //remember(!) such native requests bypass persistent context in Hibernate
    @Query(value = "SELECT u.* FROM users u WHERE u.username = :username",
            nativeQuery = true)
    List<User> findAllByUsername(String username);

    @Modifying(clearAutomatically = true)
    @Query("update User u " +
            "set u.role = :role " +
            "where u.id in (:ids)")
    int updateRole(Role role, Long... ids);

    Optional<User> findByUsername(String username);
}
