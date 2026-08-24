package com.inditex.prices.price.infrastructure.adapter.in.rest.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.inditex.prices.price.application.exception.ApplicablePriceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class PriceExceptionHandler {

    private final ProblemDetailFactory problemDetailFactory;

    @ExceptionHandler(ApplicablePriceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleApplicablePriceNotFound(
        ApplicablePriceNotFoundException exception,
        HttpServletRequest request
    ) {
        ProblemDetail problem = problemDetailFactory.create(
            HttpStatus.NOT_FOUND,
            "Applicable price not found",
            exception.getMessage(),
            request
        );

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        Map<String, List<String>> errors = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    LinkedHashMap::new,
                    Collectors.mapping(
                        FieldError::getDefaultMessage,
                        Collectors.toList()
                    )
                )
            );

        ProblemDetail problem = problemDetailFactory.create(
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            "One or more request parameters are invalid",
            request
        );

        problem.setProperty("errors", errors);

        return ResponseEntity
            .badRequest()
            .body(problem);
    }
}