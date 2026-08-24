package com.inditex.prices.price.infrastructure.adapter.in.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceResult;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceQuery;
import com.inditex.prices.price.application.port.in.query.find.FindApplicablePriceUseCase;
import com.inditex.prices.price.infrastructure.adapter.in.rest.dto.request.FindApplicablePriceRequest;
import com.inditex.prices.price.infrastructure.adapter.in.rest.dto.response.FindApplicablePriceResponse;
import com.inditex.prices.price.infrastructure.adapter.in.rest.mapper.PriceRestMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceQueryController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;
    private final PriceRestMapper priceRestMapper;

    @GetMapping("/applicable")
    public ResponseEntity<FindApplicablePriceResponse> findApplicablePrice(
        @Valid @ModelAttribute FindApplicablePriceRequest request
    ) {
        FindApplicablePriceQuery query =
            priceRestMapper.toQuery(request);

        FindApplicablePriceResult result =
            findApplicablePriceUseCase.execute(query);

        FindApplicablePriceResponse response =
            priceRestMapper.toResponse(result);

        return ResponseEntity.ok(response);
    }
}
