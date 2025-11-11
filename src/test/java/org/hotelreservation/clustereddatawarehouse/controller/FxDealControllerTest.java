package org.hotelreservation.clustereddatawarehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hotelreservation.clustereddatawarehouse.exception.DuplicateDealException;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealResponse;
import org.hotelreservation.clustereddatawarehouse.service.FxDealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FxDealController.class)
class FxDealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FxDealService fxDealService;

    @Test
    void createDeal_Success() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "FX001",
                "USD",
                "EUR",
                LocalDateTime.of(2025, 11, 10, 12, 30),
                new BigDecimal("2000.00")
        );

        FxDealResponse response = new FxDealResponse(
                "FX001",
                "USD",
                "EUR",
                request.dealTimestamp(),
                request.dealAmount(),
                LocalDateTime.now()
        );

        when(fxDealService.createDeal(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dealUniqueId").value("FX001"))
                .andExpect(jsonPath("$.fromCurrencyIsoCode").value("USD"))
                .andExpect(jsonPath("$.toCurrencyIsoCode").value("EUR"))
                .andExpect(jsonPath("$.dealAmount").value(2000.00));
    }

    @Test
    void createDeal_RejectSameCurrency() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "FX124",
                "USD",
                "USD",
                LocalDateTime.of(2025, 11, 10, 12, 30),
                new BigDecimal("2000.00")
        );


        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors[0].message", containsString("different")));
    }

    @Test
    void createDeal_RejectInvalidCurrencyCode() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "F143434",
                "USD",
                "LKJ",
                LocalDateTime.of(2025, 11, 10, 21, 0),
                new BigDecimal("1500.00")
        );


        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void createDeal_RejectDuplicateDeal() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "FX001",
                "USD",
                "EUR",
                LocalDateTime.of(2025, 11, 10, 12, 30),
                new BigDecimal("2000.00")
        );

        when(fxDealService.createDeal(any()))
                .thenThrow(new DuplicateDealException("Deal with ID FX001 already exists"));

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Duplicate Deal"))
                .andExpect(jsonPath("$.message").value("Deal with ID FX001 already exists"));
    }

    @Test
    void createDeal_RejectMissingDealUniqueId() throws Exception {

        String invalidJson = """
            {
                "fromCurrencyIsoCode": "USD",
                "toCurrencyIsoCode": "EUR",
                "dealTimestamp": "2025-11-10T12:30:00",
                "dealAmount": 2000.00
            }
            """;

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createDeal_RejectBlankDealUniqueId() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "",
                "USD",
                "EUR",
                LocalDateTime.of(2025, 11, 10, 12, 30),
                new BigDecimal("2000.00")
        );

        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDeal_RejectNegativeAmount() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "FX002",
                "USD",
                "EUR",
                LocalDateTime.of(2025, 11, 10, 12, 30),
                new BigDecimal("-1000.00")
        );


        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDeal_RejectZeroAmount() throws Exception {

        FxDealRequest request = new FxDealRequest(
                "FX003",
                "USD",
                "EUR",
                LocalDateTime.of(2025, 11, 10, 12, 30),
                new BigDecimal("0.00")
        );


        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDeal_RejectNullTimestamp() throws Exception {

        String invalidJson = """
            {
                "dealUniqueId": "FX004",
                "fromCurrencyIsoCode": "USD",
                "toCurrencyIsoCode": "EUR",
                "dealAmount": 2000.00
            }
            """;


        mockMvc.perform(post("/api/v1/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDeal_AcceptValidCurrencyPairs() throws Exception {

        String[][] validPairs = {
                {"USD", "EUR"},
                {"GBP", "JPY"},
                {"EUR", "CHF"},
                {"CAD", "AUD"}
        };

        for (String[] pair : validPairs) {
            FxDealRequest request = new FxDealRequest(
                    "DEAL_" + pair[0] + "_" + pair[1],
                    pair[0],
                    pair[1],
                    LocalDateTime.of(2025, 11, 10, 12, 30),
                    new BigDecimal("1000.00")
            );

            FxDealResponse response = new FxDealResponse(
                    request.dealUniqueId(),
                    pair[0],
                    pair[1],
                    request.dealTimestamp(),
                    request.dealAmount(),
                    LocalDateTime.now()
            );

            when(fxDealService.createDeal(any())).thenReturn(response);


            mockMvc.perform(post("/api/v1/deals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }
}


