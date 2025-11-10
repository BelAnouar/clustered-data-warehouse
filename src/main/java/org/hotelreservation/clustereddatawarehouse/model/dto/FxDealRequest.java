package org.hotelreservation.clustereddatawarehouse.model.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;





public record FxDealRequest(
        @NotBlank String dealUniqueId,
        @NotBlank  String fromCurrencyIsoCode,
        @NotBlank  String toCurrencyIsoCode,
        @NotNull LocalDateTime dealTimestamp,
        @NotNull  BigDecimal dealAmount
) {}