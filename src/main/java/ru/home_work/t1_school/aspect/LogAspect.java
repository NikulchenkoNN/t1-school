package ru.home_work.t1_school.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.aspect.annotations.LogException;
import ru.home_work.t1_school.aspect.annotations.LogMethodCallWithParams;
import ru.home_work.t1_school.aspect.annotations.LogExecutionTime;
import ru.home_work.t1_school.aspect.annotations.LogReturning;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Before("@annotation(logMethodCallWithParams)")
    void logCalls(JoinPoint joinPoint, LogMethodCallWithParams logMethodCallWithParams) {
        String message = String.format("Метод %s класса %s вызван с параметрами %s",
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType().getName(),
                Arrays.toString(joinPoint.getArgs()));
        log.info(message);
    }

    @Around("@annotation(logExecutionTime)")
    public Object logTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long stop = System.currentTimeMillis();
        String message = String.format("Метод %s класса %s выполнялся %d мс",
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType().getName(),
                stop - start);
        log.info(message);
        return result;
    }

    @AfterThrowing(
            pointcut = "@annotation(logException)",
            throwing = "exception"
    )
    void logException(JoinPoint joinPoint, LogException logException, Throwable exception) {
        String message = String.format("Метод %s класса %s выбросил исключение %s",
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType().getName(),
                exception);
        log.info(message);
    }

    @AfterReturning(
            pointcut = "@annotation(logReturning)",
            returning = "val"
    )
    void logReturning(JoinPoint joinPoint, LogReturning logReturning, Object val) {
        String message = String.format("Метод %s класса %s вернул значение %s",
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType().getName(),
                val
        );
        log.info(message);
    }

}
