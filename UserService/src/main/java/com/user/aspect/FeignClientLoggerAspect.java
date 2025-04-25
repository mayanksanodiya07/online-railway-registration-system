package com.user.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeignClientLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientLoggerAspect.class);

    @Pointcut("execution(* com.user.client.*.*(..))")
    public void feignClientMethods() {}

    @Before("feignClientMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("[FeignClient] ➡️ {} with arguments: {}",
        		joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "feignClientMethods()", returning = "response")
    public void logAfterReturning(JoinPoint joinPoint, Object response) {
        logger.info("[FeignClient] ✅ {} with result: {}",
                joinPoint.getSignature().toShortString(),
                response);
    }
    

    @AfterThrowing(pointcut = "feignClientMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("[FeignClient] ❌ {} threw: {} | {}",
                joinPoint.getSignature().getName(),
                ex.getMessage(), ex.getClass().getSimpleName());
    }
    
    @Around("feignClientMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        logger.info("[FeignClient] ⏱️ {} executed in {} ms",
                joinPoint.getSignature().getName(),
                duration);

        return result;
    }
}
