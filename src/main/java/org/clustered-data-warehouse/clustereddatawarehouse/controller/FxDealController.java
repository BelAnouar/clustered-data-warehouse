package org.hotelreservation.clustereddatawarehouse.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealResponse;
import org.hotelreservation.clustereddatawarehouse.service.FxDealService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deals")
@RequiredArgsConstructor
@Slf4j
public class FxDealController {
  private final FxDealService fxDealService;
   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public FxDealResponse createDeal(@Valid @RequestBody FxDealRequest request) {
       log.info("Received request to create FX deal: {}", request.dealUniqueId());
       return fxDealService.createDeal(request);
   }

}
