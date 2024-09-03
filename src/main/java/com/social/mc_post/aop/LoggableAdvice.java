package com.social.mc_post.aop;

import com.social.mc_post.dto.PostDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

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
        log.info("Получение постов {}", joinPoint.getSignature().getName());
    }

}
