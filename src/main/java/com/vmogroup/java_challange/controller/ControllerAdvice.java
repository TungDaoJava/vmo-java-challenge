package com.vmogroup.java_challange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(produces = "application/json", exception = {ResponseStatusException.class})
    public ResponseEntity<ProblemDetail> responseStatusException(ResponseStatusException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getReason());

        if (exception.getStatusCode().is4xxClientError()) {
            problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/400"));
        } else if (exception.getStatusCode().is5xxServerError()) {
            problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500"));
        }


        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(produces = "application/json", exception = {RuntimeException.class})
    public ResponseEntity<ProblemDetail> unexpectedException(){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Application Error");
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/500"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

}
