package com.yadev.spring.database.repository;

import com.yadev.spring.database.entity.Company;
import com.yadev.spring.database.entity.Role;
import com.yadev.spring.database.entity.User;
import com.yadev.spring.dto.PersonalInfo;
import com.yadev.spring.dto.UserFilter;
import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter userFilter);

    List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role);

    void updateCompanyAndRole(List<User> users);

    void updateCompanyAndRoleNamed(List<User> users);
}
