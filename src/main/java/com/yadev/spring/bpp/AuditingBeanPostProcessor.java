package com.yadev.spring.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;

@Component
public class AuditingBeanPostProcessor implements BeanPostProcessor {

    private final HashMap<String, Class<?>> auditBeans = new HashMap<>();
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Auditing.class)){
            auditBeans.put(beanName, bean.getClass());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = auditBeans.get(beanName);
        if (beanClass != null){
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(),
                    (proxy, method, args) -> {
                        System.out.println("Audit method " + method.getName());
                        var start = System.nanoTime();
                        try {
                            return method.invoke(bean, args);
                        }finally {
                            var end = System.nanoTime();
                            System.out.println("time execution: " + (end - start));
                        }
                    });
        }
        return bean;
    }
}
