package com.yadev.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@Order(2)
public class SecondAspect {
    @Around("com.yadev.spring.aop.FirstAspect.anyFindByIdServiceMethod() && target(service) && args(id)")
    public Object addLoggingAroung(ProceedingJoinPoint proceedingJoinPoint,
                                   Object service,
                                   Object id) throws Throwable {
        log.info("AROUND before - invoked findById method in class {} with id {}", service, id);
        try{
            var result = proceedingJoinPoint.proceed();
            log.info("AROUND after returning - invoked findById method in class {} with result {}", service, result);
            return result;
        }catch (Throwable ex){
            log.info("AROUND after throwing - invoked findById method in class {} with exception {} : {}", service, ex.getClass(), ex.getMessage());
            throw ex;
        } finally {
            log.info("AROUND after (finally) - invoked findById method in class {}", service);
        }
    }
}
