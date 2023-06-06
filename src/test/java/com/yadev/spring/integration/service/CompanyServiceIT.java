package com.yadev.spring.integration.service;

import com.yadev.spring.integration.annotation.IT;
import com.yadev.spring.config.DatabaseProperties;
import com.yadev.spring.dto.CompanyReadDto;
import com.yadev.spring.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@IT
@RequiredArgsConstructor
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) //substituted by spring.properties
//@ExtendWith(SpringExtension.class)                                    //@TestConstructor includes this.
//@ContextConfiguration(classes = ApplicationRunner.class,              //@TestConstructor includes this.
//        initializers = ConfigDataApplicationContextInitializer.class) //@TestConstructor includes this.
public class CompanyServiceIT {

    private static final Integer COMPANY_ID = 1;

    private final CompanyService companyService;
    private final DatabaseProperties databaseProperties;

    @Test
    void findById(){
        var actualResult = companyService.findById(COMPANY_ID);

        assertTrue(actualResult.isPresent());

        var expectedResult = new CompanyReadDto(COMPANY_ID, null);
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}
