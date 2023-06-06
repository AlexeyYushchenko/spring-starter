package com.yadev.spring.database.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.yadev.spring.database.entity.Role;
import com.yadev.spring.database.entity.User;
import com.yadev.spring.database.querydsl.QPredicates;
import com.yadev.spring.dto.PersonalInfo;
import com.yadev.spring.dto.UserFilter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.List;
import java.util.Map;
import static com.yadev.spring.database.entity.QUser.user;


@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FIND_BY_COMPANY_AND_ID = """
            SELECT 
                firstname,
                lastname, 
                birth_date
            FROM 
                users
            WHERE 
                company_id = ? AND role = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE = """
            UPDATE users
            SET 
                company_id = ?,
                role = ?
            WHERE 
                id = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
            UPDATE users
            SET 
                company_id = :companyId,
                role = :role
            WHERE 
                id = :id
            """;

    @Override
    public List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role) {
        return jdbcTemplate.query(FIND_BY_COMPANY_AND_ID, (rs, rowNum)
                -> new PersonalInfo(
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getDate("birth_date").toLocalDate()
        ), companyId, role.name());
    }

    @Override
    public void updateCompanyAndRole(List<User> users) {
        var args = users.stream()
                .map(user -> new Object[]{user.getCompany().getId(), user.getRole().name(), user.getId()})
                .toList();
        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, args);
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        var args = users.stream()
                .map(user -> Map.of(
                        "companyId", user.getCompany().getId(),
                        "role", user.getRole().name(),
                        "id", user.getId()
                ))
                .map(MapSqlParameterSource::new)
                .toArray(MapSqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE_NAMED, args);
    }

    @Override
    public List<User> findAllByFilter(UserFilter filter) {

//        return Collections.EMPTY_LIST;
        //QUERYDSL
        var predicates = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .build();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicates)
                .fetch();

        //CRITERIA API
//        var cb = entityManager.getCriteriaBuilder();
//        var criteria = cb.createQuery(User.class);
//
//        var user = criteria.from(User.class);
//        List<Predicate> predicates = new ArrayList<>();
//        if (userFilter.firstname() != null){
//            predicates.add(cb.like(user.get("firstname"), userFilter.firstname()));
//        }
//        if (userFilter.lastname() != null){
//            predicates.add(cb.like(user.get("lastname"), userFilter.lastname()));
//        }
//        if (userFilter.birthDate() != null){
//            predicates.add(cb.lessThanOrEqualTo(user.get("birthDate"), userFilter.birthDate()));
//        }
//
//        criteria.select(user)
//                .where(
//                        predicates.toArray(new Predicate[0])
//                );
//
//        return entityManager.createQuery(criteria)
//                .getResultList();
    }
}
