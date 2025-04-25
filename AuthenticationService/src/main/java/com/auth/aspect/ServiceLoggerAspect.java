package com.auth.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLoggerAspect.class);

    // Pointcut for all methods in services
    @Pointcut("within(com.auth.service..*)")
    public void serviceMethods() {}

    // Log method start and input args
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("[Service]     ➡️ {} with arguments: {}",
        		joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    // Log method return
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("[Service]     ✅ {} with result: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    // Log exceptions
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("[Service]     ❌ {} threw: {} | {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage(), ex.getClass().getSimpleName());
    }

    // Log execution time
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // proceed with method execution
        long duration = System.currentTimeMillis() - start;

        logger.info("[Service]     ⏱️ {} executed in {} ms",
//                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().toShortString(),
                duration);

        return result;
    }
}
