package com.social.mc_post.controllers;


import com.social.mc_post.exception.ErrorPost;
import com.social.mc_post.exception.PostNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler
    public ResponseEntity<ErrorPost> postNotFoundException(PostNotFoundException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorPost(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
