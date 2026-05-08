package com.julian.spring_demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.julian.spring_demo.service.*.*(..))")
    public Object logExecutionTime (ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.debug("AOP: Starting {}.{}()", className, methodName);

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        log.debug("AOP: {}.{}() completed in {}ms", className, methodName, duration);

        return result;
    }
}
