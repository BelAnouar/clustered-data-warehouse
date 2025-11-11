package org.hotelreservation.clustereddatawarehouse.service;


import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealRequest;
import org.hotelreservation.clustereddatawarehouse.model.dto.FxDealResponse;

public interface FxDealService {
      FxDealResponse createDeal(FxDealRequest request);
}
