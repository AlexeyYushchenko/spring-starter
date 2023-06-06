package com.yadev.spring.integration.annotation;

import com.yadev.spring.bpp.Transaction;
import com.yadev.spring.integration.TestApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationRunner.class)
@Transactional
public @interface IT {
}
