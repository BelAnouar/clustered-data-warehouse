package org.hotelreservation.clustereddatawarehouse.service;
import org.hotelreservation.clustereddatawarehouse.exception.DuplicateDealException;
import org.hotelreservation.clustereddatawarehouse.mapper.FxDealMapper;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealResponse;
import org.hotelreservation.clustereddatawarehouse.model.entity.FxDeal;
import org.hotelreservation.clustereddatawarehouse.repository.FxDealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FxDealServiceTest {

    @Mock
    private FxDealRepository fxDealRepository;

    @Mock
    private FxDealMapper dealMapper;

    @InjectMocks
    private FxDealServiceImpl fxDealService;

    private FxDealRequest validRequest;
    private FxDeal validDeal;
    private FxDealResponse validResponse;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        validRequest = new FxDealRequest(
                "DEAL001",
                "USD",
                "EUR",
                now.minusHours(1),
                new BigDecimal("1000.00")
        );

        validDeal = FxDeal.builder()
                .dealUniqueId("DEAL001")
                .fromCurrencyIsoCode("USD")
                .toCurrencyIsoCode("EUR")
                .dealTimestamp(validRequest.dealTimestamp())
                .dealAmount(validRequest.dealAmount())
                .createdAt(now)
                .build();

        validResponse = new FxDealResponse(
                "DEAL001",
                "USD",
                "EUR",
                validRequest.dealTimestamp(),
                validRequest.dealAmount(),
                now
        );
    }

    @Test
    void createDeal_Success() {

        when(fxDealRepository.existsByDealUniqueId("DEAL001")).thenReturn(false);
        when(dealMapper.toEntity(validRequest)).thenReturn(validDeal);
        when(fxDealRepository.save(validDeal)).thenReturn(validDeal);
        when(dealMapper.toResponse(validDeal)).thenReturn(validResponse);


        FxDealResponse result = fxDealService.createDeal(validRequest);


        assertThat(result).isNotNull();
        assertThat(result.dealUniqueId()).isEqualTo("DEAL001");
        assertThat(result.fromCurrencyIsoCode()).isEqualTo("USD");
        assertThat(result.toCurrencyIsoCode()).isEqualTo("EUR");

        verify(fxDealRepository).existsByDealUniqueId("DEAL001");
        verify(fxDealRepository).save(validDeal);
        verify(dealMapper).toEntity(validRequest);
        verify(dealMapper).toResponse(validDeal);
    }

    @Test
    void createDeal_ThrowsDuplicateDealException_WhenDealExists() {

        when(fxDealRepository.existsByDealUniqueId("DEAL001")).thenReturn(true);

        assertThatThrownBy(() -> fxDealService.createDeal(validRequest))
                .isInstanceOf(DuplicateDealException.class)
                .hasMessage("Deal with ID DEAL001 already exists");

        verify(fxDealRepository).existsByDealUniqueId("DEAL001");
        verify(fxDealRepository, never()).save(any());
        verify(dealMapper, never()).toEntity(any());
    }

    @Test
    void createDeal_ThrowsDuplicateDealException_OnDataIntegrityViolation() {

        when(fxDealRepository.existsByDealUniqueId("DEAL001")).thenReturn(false);
        when(dealMapper.toEntity(validRequest)).thenReturn(validDeal);
        when(fxDealRepository.save(validDeal))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));

        assertThatThrownBy(() -> fxDealService.createDeal(validRequest))
                .isInstanceOf(DuplicateDealException.class)
                .hasMessage("Deal with ID DEAL001 already exists");

        verify(fxDealRepository).save(validDeal);
    }

    @Test
    void createDeal_WithLargeAmount() {

        FxDealRequest largeAmountRequest = new FxDealRequest(
                "DEAL002",
                "USD",
                "EUR",
                LocalDateTime.now().minusHours(1),
                new BigDecimal("999999999999999.9999")
        );

        FxDeal largeDeal = FxDeal.builder()
                .dealUniqueId(largeAmountRequest.dealUniqueId())
                .fromCurrencyIsoCode(largeAmountRequest.fromCurrencyIsoCode())
                .toCurrencyIsoCode(largeAmountRequest.toCurrencyIsoCode())
                .dealTimestamp(largeAmountRequest.dealTimestamp())
                .dealAmount(largeAmountRequest.dealAmount())
                .build();

        when(fxDealRepository.existsByDealUniqueId("DEAL002")).thenReturn(false);
        when(dealMapper.toEntity(largeAmountRequest)).thenReturn(largeDeal);
        when(fxDealRepository.save(largeDeal)).thenReturn(largeDeal);
        when(dealMapper.toResponse(largeDeal)).thenReturn(
                new FxDealResponse(largeAmountRequest.dealUniqueId(), largeAmountRequest.fromCurrencyIsoCode(), largeAmountRequest.toCurrencyIsoCode(),
                        largeAmountRequest.dealTimestamp(),
                        largeAmountRequest.dealAmount(),
                        LocalDateTime.now())
        );


        FxDealResponse result = fxDealService.createDeal(largeAmountRequest);

        assertThat(result).isNotNull();
        assertThat(result.dealAmount()).isEqualTo(new BigDecimal("999999999999999.9999"));
    }

    @Test
    void  CreateDeal_WithMinimalAmount() {
        FxDealRequest minimalAmountRequest = new FxDealRequest("DEAL004",
                "EUR","USD",LocalDateTime.now().minusHours(1),
                new BigDecimal("0.2"));
        FxDeal minimalDeal=FxDeal.builder()
                .dealUniqueId(minimalAmountRequest.dealUniqueId())
                .fromCurrencyIsoCode(minimalAmountRequest.fromCurrencyIsoCode())
                .toCurrencyIsoCode(minimalAmountRequest.toCurrencyIsoCode())
                .dealTimestamp(minimalAmountRequest.dealTimestamp())
                .dealAmount(minimalAmountRequest.dealAmount())
                .createdAt(LocalDateTime.now()).build();
        when(fxDealRepository.existsByDealUniqueId("DEAL004")).thenReturn(false);
        when(dealMapper.toEntity(minimalAmountRequest)).thenReturn(minimalDeal);
        when(fxDealRepository.save(minimalDeal)).thenReturn(minimalDeal);
        when(dealMapper.toResponse(minimalDeal)).thenReturn(
                new FxDealResponse("DEAL004",
                        "EUR","USD",minimalAmountRequest.dealTimestamp(),
                        minimalAmountRequest.dealAmount(),LocalDateTime.now())
        );
        FxDealResponse result = fxDealService.createDeal(minimalAmountRequest);
        assertThat(result).isNotNull();
        assertThat(result.dealAmount()).isEqualTo(new BigDecimal("0.2"));
    }
}
