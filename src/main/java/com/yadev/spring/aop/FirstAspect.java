package com.yadev.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
@Order(1)
public class FirstAspect {

    @Pointcut("this(org.springframework.data.repository.Repository)")
    public void isRepositoryLayer() {

    }

    @Pointcut("com.yadev.spring.aop.CommonAspect.isControllerLayer() && @annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void hasGetMapping() {
    }

    @Pointcut("com.yadev.spring.aop.CommonAspect.isControllerLayer() && args(org.springframework.ui.Model, ..)")
    public void hasModelParam() {
    }

    @Pointcut("com.yadev.spring.aop.CommonAspect.isControllerLayer() && @args(com.yadev.spring.validation.UserInfo, ..)")
    public void hasUserInfoAnnotation() {

    }

    @Pointcut("bean(*Service)")
    public void isServiceLayerBean() {

    }

    @Pointcut("execution(public * com.yadev.spring.service.*Service.findById(*))")
    public void anyFindByIdServiceMethod() {

    }

    @Before("anyFindByIdServiceMethod() " +
            "&& args(id) " +
            "&& target(service) " +
            "&& this(serviceProxy)" +
            "&& @within(transactional)")
    public void addLogging(JoinPoint joinPoint,
                           Object id,
                           Object service,
                           Object serviceProxy,
                           Transactional transactional) {
        log.info("before - invoked findById method in class {} with id {}", service, id);
    }

    @AfterReturning(value = "anyFindByIdServiceMethod()" +
            "&& target(service)",
            returning = "result")
    public void addLogginAfterReturning(Object service,
                                        Object result) {
        log.info("after returning - invoked findById method in class {} with result {}", service, result);
    }

    @AfterThrowing(value = "anyFindByIdServiceMethod()" +
            "&& target(service)",
            throwing = "ex")
    public void addLogginAfterThrowing(Object service,
                                       Exception ex) {
        log.info("after throwing - invoked findById method in class {} with exception {} : {}", service, ex.getClass(), ex.getMessage());
    }

    @After(value = "anyFindByIdServiceMethod() && target(service)")
    public void addLogginAfterFinally(Object service) {
        log.info("after (finally) - invoked findById method in class {}", service);
    }

}
