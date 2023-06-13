package com.yadev.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class CommonAspect {
    /*
        '@within' viene utilizzato per selezionare tutti i metodi all'interno delle classi
        che sono annotate con una determinata annotazione.
        (qui - con l'annotatione 'org.springframework.stereotype.Controller')
     */

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void isControllerLayer() {

    }

    /*
        'within' viene utilizzato per selezionare tutti i metodi all'interno delle classi
        che appartengono a un determinato package o a una determinata classe.
    */

    @Pointcut("within(com.yadev.spring.service..*Service)")
    public void isServiceLayer() {

    }
}
