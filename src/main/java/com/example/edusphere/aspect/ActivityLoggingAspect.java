package com.example.edusphere.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ActivityLoggingAspect {

    @AfterReturning(
        pointcut = "execution(* com.example.edusphere.service.*.*(..))",
        returning = "result"
    )
    public void logActivity(JoinPoint joinPoint, Object result) {

        System.out.println(
            "AOP LOG: Method executed - "
            + joinPoint.getSignature().toShortString()
        );
    }
}