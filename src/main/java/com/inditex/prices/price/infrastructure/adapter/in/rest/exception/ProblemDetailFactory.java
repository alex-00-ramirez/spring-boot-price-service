package com.inditex.prices.price.infrastructure.adapter.in.rest.exception;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public final class ProblemDetailFactory {

    public ProblemDetail create(
        HttpStatus status,
        String title,
        String detail,
        HttpServletRequest request
    ) {
        ProblemDetail problem =
            ProblemDetail.forStatusAndDetail(status, detail);

        problem.setTitle(title);
        problem.setInstance(
            URI.create(request.getRequestURI())
        );
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }
}