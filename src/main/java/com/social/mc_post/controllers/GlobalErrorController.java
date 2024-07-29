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
public class GlobalErrorController {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorPost> postNotFoundException(PostNotFoundException ex){
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorPost(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
