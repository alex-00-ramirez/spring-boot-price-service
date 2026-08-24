package com.inditex.prices.price.infrastructure.adapter.in.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceQueryControllerIntegrationTest {

    private static final String ENDPOINT = "/api/v1/prices/applicable";

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "{index}: {0} returns price list {1}")
    @MethodSource("applicablePriceScenarios")
    @DisplayName("Should return the applicable price")
    void shouldReturnApplicablePrice(
        String applicationDate,
        long expectedPriceList,
        double expectedAmount,
        String expectedStartDate,
        String expectedEndDate
    ) throws Exception {
        mockMvc.perform(
            get(ENDPOINT)
                .queryParam(
                    "applicationDate",
                    applicationDate
                )
                .queryParam("productId", "35455")
                .queryParam("brandId", "1")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId").value(35455))
            .andExpect(jsonPath("$.brandId").value(1))
            .andExpect(
                jsonPath("$.priceList")
                    .value(expectedPriceList)
            )
            .andExpect(
                jsonPath("$.startDate")
                    .value(expectedStartDate)
            )
            .andExpect(
                jsonPath("$.endDate")
                    .value(expectedEndDate)
            )
            .andExpect(
                jsonPath("$.amount")
                    .value(expectedAmount)
            )
            .andExpect(jsonPath("$.currency").value("EUR"));
    }

    private static Stream<Arguments> applicablePriceScenarios() {
        return Stream.of(
            Arguments.of(
                "2020-06-14T10:00:00",
                1L,
                35.50,
                "2020-06-14T00:00:00",
                "2020-12-31T23:59:59"
            ),
            Arguments.of(
                "2020-06-14T16:00:00",
                2L,
                25.45,
                "2020-06-14T15:00:00",
                "2020-06-14T18:30:00"
            ),
            Arguments.of(
                "2020-06-14T21:00:00",
                1L,
                35.50,
                "2020-06-14T00:00:00",
                "2020-12-31T23:59:59"
            ),
            Arguments.of(
                "2020-06-15T10:00:00",
                3L,
                30.50,
                "2020-06-15T00:00:00",
                "2020-06-15T11:00:00"
            ),
            Arguments.of(
                "2020-06-16T21:00:00",
                4L,
                38.95,
                "2020-06-15T16:00:00",
                "2020-12-31T23:59:59"
            )
        );
    }

    @Test
    @DisplayName("Should return 404 when no applicable price exists")
    void shouldReturnNotFoundWhenNoApplicablePriceExists()
        throws Exception {

        mockMvc.perform(
            get(ENDPOINT)
                .queryParam(
                    "applicationDate",
                    "2020-06-14T10:00:00"
                )
                .queryParam("productId", "99999")
                .queryParam("brandId", "1")
        )
            .andExpect(status().isNotFound())
            .andExpect(
                jsonPath("$.title")
                    .value("Applicable price not found")
            )
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(
                jsonPath("$.detail")
                    .value(
                        "No applicable price found for product 99999, "
                            + "brand 1 and date 2020-06-14T10:00"
                    )
            )
            .andExpect(
                jsonPath("$.instance")
                    .value(ENDPOINT)
            );
    }

    @Test
    @DisplayName("Should return 400 when product identifier is invalid")
    void shouldReturnBadRequestWhenProductIdIsInvalid()
        throws Exception {

        mockMvc.perform(
            get(ENDPOINT)
                .queryParam(
                    "applicationDate",
                    "2020-06-14T10:00:00"
                )
                .queryParam("productId", "0")
                .queryParam("brandId", "1")
        )
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.title")
                    .value("Validation failed")
            )
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(
                jsonPath("$.errors.productId[0]")
                    .value(
                        "Product identifier must be greater than zero"
                    )
            )
            .andExpect(
                jsonPath("$.instance")
                    .value(ENDPOINT)
            );
    }
}
