package ru.home_work.t1_school.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.home_work.t1_school.aspect.annotations.LogExecution;
import ru.home_work.t1_school.aspect.annotations.LogExecutionTime;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Before("@annotation(annotation)")
    void logExecution(JoinPoint joinPoint, LogExecution annotation) {
        String message = String.format("Метод %s класса %s вызван с параметрами %s",
                joinPoint.getSignature().getName(),
                joinPoint.getSignature().getDeclaringType().getName(),
                Arrays.toString(joinPoint.getArgs()));
        log.info(message);
    }

    @Around("@annotation(annotation)")
    public Object logTime(ProceedingJoinPoint joinPoint, LogExecutionTime annotation) throws Throwable {
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
}
