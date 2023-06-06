package com.yadev.spring.dto;

import com.yadev.spring.database.entity.Role;
import com.yadev.spring.validation.UserInfo;
import com.yadev.spring.validation.group.CreateAction;
import com.yadev.spring.validation.group.UpdateAction;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Value
@FieldNameConstants
@UserInfo(groups = UpdateAction.class)
public class UserCreateEditDto {
    @Email
    String username;

    @NotBlank(groups = CreateAction.class)
    String rawPassword;

    @DateTimeFormat(pattern = "yyyy-MM-dd") //WebConfiguration implements WebMvcConfigurer is used instead
    LocalDate birthDate;

    @Size(min = 2, max = 32)
    String firstname;

    String lastname;

    Role role;

    Integer companyId;

    MultipartFile image;
}
