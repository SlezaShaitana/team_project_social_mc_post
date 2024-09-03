package com.social.mc_post.aop;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.AuthenticationException;
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

    @Pointcut(value = "@annotation(com.social.mc_post.aop.JwtTokenException)")
    public void jwtException(){
    }

    @AfterThrowing(pointcut = "jwtException()", throwing = "ex")
    public void afterThrowingAdvice(Exception ex) {

        if (ex instanceof IllegalArgumentException){
            log.error("Request is empty or damaged: {}", ex.getLocalizedMessage());
        }
        else if (ex instanceof MalformedJwtException){
            log.error("Invalid JWT token format: {}", ex.getMessage());
        }
        else if(ex instanceof Exception){
            log.error("JWT token validation failed: {}", ex.getMessage());
        }
    }

}
