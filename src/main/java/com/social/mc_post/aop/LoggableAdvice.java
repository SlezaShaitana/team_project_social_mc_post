package com.social.mc_post.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggableAdvice {

    @Pointcut(value = "execution(* publishingDeferredPosts())")
    public void logMethodPublishPointcut() {
    }

    @Before("logMethodPublishPointcut()")
    public void beforeLoggingMethodPublish(JoinPoint joinPoint){
        log.info("Поиск отложенных постов {}", joinPoint.getTarget().getClass().getSimpleName());
    }

    @After("logMethodPublishPointcut()")
    public void afterLoggingMethodPublish(JoinPoint joinPoint) {
        log.info("Публикация отложенных постов {}", joinPoint.getTarget().getClass().getSimpleName());
    }

    @Pointcut(value = "execution(* getPosts(..))")
    public void logMethodGetPosts(){}

    @After("logMethodGetPosts()")
    public void afterLoggingMethodGetPosts(JoinPoint joinPoint) {
        log.info("Получение постов {}", joinPoint.getTarget().toString());
    }

}
