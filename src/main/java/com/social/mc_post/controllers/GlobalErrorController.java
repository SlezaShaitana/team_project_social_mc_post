package com.social.mc_post.controllers;


import com.social.mc_post.exception.AuthException;
import com.social.mc_post.exception.BadRequestException;
import com.social.mc_post.exception.ErrorApp;
import com.social.mc_post.exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalErrorController {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorApp> postNotFoundException(PostNotFoundException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorApp(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorApp> badRequestException(BadRequestException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorApp(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorApp> authException(AuthException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorApp(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
