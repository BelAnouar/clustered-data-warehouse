package org.hotelreservation.clustereddatawarehouse.model.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hotelreservation.clustereddatawarehouse.validation.annotation.DifferentCurrencies;
import org.hotelreservation.clustereddatawarehouse.validation.annotation.ValidIsoCurrency;

import java.math.BigDecimal;
import java.time.LocalDateTime;




@DifferentCurrencies
public record FxDealRequest(
        @NotBlank String dealUniqueId,
        @NotBlank @ValidIsoCurrency String fromCurrencyIsoCode,
        @NotBlank @ValidIsoCurrency String toCurrencyIsoCode,
        @NotNull LocalDateTime dealTimestamp,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal dealAmount
) {}