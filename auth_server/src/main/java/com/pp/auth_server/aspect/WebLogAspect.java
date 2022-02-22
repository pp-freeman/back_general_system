package com.pp.auth_server.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * web日志切面
 *
 * @author pengpan
 * @date 2020/12/14
 */
@Aspect
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(* com.pp.auth_server.controller.*.*(..))")
    public void logPointCut(){

    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记录下请求内容
        logger.info("请求地址:"+request.getRequestURI().toString());
        logger.info("HTTP Method:"+request.getMethod());
        //获取真实的请求地址
        logger.info("CLASS_METHOD:"+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        logger.info("参数:"+ Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "ret",pointcut = "logPointCut()")
    public void doAfterReturning(Object ret) throws Throwable{
        //处理完请求，返回内容
        logger.info("返回值:"+ret);
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable{
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();
        logger.info("耗时:"+(System.currentTimeMillis()-startTime));
        return ob;
    }

}
