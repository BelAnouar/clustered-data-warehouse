package org.hotelreservation.clustereddatawarehouse.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FxDealResponse(String dealUniqueId,
                             String fromCurrencyIsoCode,
                             String toCurrencyIsoCode,
                             LocalDateTime dealTimestamp,
                             BigDecimal dealAmount,
                             LocalDateTime createdAt) {
}
