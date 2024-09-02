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

    @Pointcut(value = "execution(publishingDeferredPosts())")
    public void allLoginMethodPublishPointcut() {
    }

    @Before("allLoginMethodPublishPointcut()")
    public void beforeLoggingMethodPublish(JoinPoint joinPoint){
        log.info("Поиск отложенных постов {}", joinPoint.getTarget().getClass().getSimpleName());
    }

    @After("allLoginMethodPublishPointcut()")
    public void afterLoggingMethodPublish(JoinPoint joinPoint) {
        log.info("Публикация отложенных постов {}", joinPoint.getTarget().getClass().getSimpleName());
    }


}
