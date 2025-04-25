package com.user.aspect;
  
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggerAspect.class);

    // Log all controller methods
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {}

    @Before("controller()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("[Controller]  ➡️ {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controller()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("[Controller]  ✅ {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "controller()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("[Controller]  ❌ {} threw: {} | {}", joinPoint.getSignature().toShortString(), ex.getMessage(), ex.getClass().getSimpleName());
    }

    @Around("controller()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(); // continue method execution
        long executionTime = System.currentTimeMillis() - start;

        logger.info("[Controller]  ⏱️ {} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
        return proceed;
    }
}
