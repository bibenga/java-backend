package com.mycompany.test2.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody Map<String, Object> handleConstraintViolation(
            ConstraintViolationException e,
            ServletWebRequest request) {
        var result = new LinkedHashMap<String, Object>();
        result.put("type", "ConstraintViolationException");
        result.put("timestamp", ZonedDateTime.now());
        result.put("path", request.getRequest().getRequestURI());
        result.put("status", HttpStatus.BAD_REQUEST.value());
        result.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        result.put("message", e.getMessage());
        // result.put("errors", e.getConstraintViolations().stream()
        // .map(cv -> SimpleObjectError.from(cv, messageSource, request.getLocale())));
        return result;
    }

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(EntityNotFoundException.class)
    // public @ResponseBody Map<String, Object>
    // handleEntityNotFoundException(ConstraintViolationException e,
    // ServletWebRequest request) {
    // var result = new HashMap<String, Object>();
    // result.put("timestamp", ZonedDateTime.now());
    // result.put("path", request.getRequest().getRequestURI());
    // result.put("status", HttpStatus.BAD_REQUEST.value());
    // result.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
    // result.put("message", e.getMessage());
    // // result.put("errors", e.getConstraintViolations().stream()
    // // .map(cv -> SimpleObjectError.from(cv, messageSource,
    // request.getLocale())));
    // return result;
    // }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SubscriptionNotFound.class)
    @ResponseBody
    public ProblemDetail handleSubscriptionNotFound(
            SubscriptionNotFound e,
            ServletWebRequest request) {
        // ErrorResponse
        // var result = new LinkedHashMap<String, Object>();
        // result.put("type", "SubscriptionNotFound");
        // result.put("timestamp", ZonedDateTime.now());
        // result.put("path", request.getRequest().getRequestURI());
        // result.put("status", HttpStatus.BAD_REQUEST.value());
        // result.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        // result.put("message", e.getMessage());

        // result.put("errors", e.getConstraintViolations().stream()
        // .map(cv -> SimpleObjectError.from(cv, messageSource, request.getLocale())));
        var detail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        detail.setTitle("SubscriptionNotFound");
        detail.setDetail(e.getMessage());
        detail.setProperty("timestamp", ZonedDateTime.now());
        detail.setProperty("opa", "Si");

        return detail;
    }
}
