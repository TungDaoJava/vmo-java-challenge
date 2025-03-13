package com.vmogroup.java_challange.monitor;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut for all methods inside @Service classes
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger classLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String methodName = joinPoint.getSignature().getName();

        classLogger.info("Entering method: {}() with arguments: {}", methodName, joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed();
            classLogger.info("Exiting method: {} \nReturned: {}", methodName, result);
            return result;
        } catch (Throwable ex) {
            classLogger.error("Exception in method: {} \nError: {}\n", methodName, ex.getMessage(), ex);
            throw ex;
        }
    }

}
